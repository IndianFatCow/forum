package org.example.forum_platform.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.forum_platform.service.SecurityUserDetailsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private SecurityUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        logger.info("=== JWT 过滤器开始处理请求 ===");
        logger.info("请求方法: {}", request.getMethod());
        logger.info("请求URL: {}", request.getRequestURL());
        logger.info("客户端IP: {}", request.getRemoteAddr());
        logger.info("User-Agent: {}", request.getHeader("User-Agent"));

        String header = request.getHeader("Authorization");
        String token = null;
        String username = null;

        // 检查 Authorization 头
        if (header == null) {
            logger.warn("未找到 Authorization 头");
        } else if (header.startsWith("Bearer ")) {
            token = header.substring(7);
            logger.info("提取到 Token，长度: {} 字符", token.length());

            try {
                username = jwtTokenUtil.getUsernameFromToken(token);
                logger.info("Token 解析成功 - 用户名: {}", username);
            } catch (Exception e) {
                logger.error("Token 解析失败: {}", e.getMessage());
                logger.debug("Token 内容: {}", token);
            }
        } else {
            logger.warn("Authorization 头格式错误，应以 'Bearer ' 开头");
            logger.debug("Authorization 头内容: {}", header);
        }

        // 验证 token 和用户认证
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            logger.info("开始验证用户: {}", username);

            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                logger.info("用户查询成功 - 角色: {}", userDetails.getAuthorities());

                if (jwtTokenUtil.validateToken(token)) {
                    logger.info("Token 验证成功");

                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(auth);

                    logger.info("用户认证成功 - 已设置 SecurityContext");
                } else {
                    logger.warn("Token 验证失败");
                }
            } catch (Exception e) {
                logger.error("用户认证过程失败: {}", e.getMessage());
            }
        } else {
            if (username == null) {
                logger.info("用户名解析为空，跳过认证");
            } else {
                logger.info("SecurityContext 中已存在认证信息，跳过重复认证");
            }
        }

        logger.info("=== JWT 过滤器处理完成，继续过滤器链 ===");
        filterChain.doFilter(request, response);
    }

    // 可选：添加不过滤的路径（如公开接口）
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();

        // 这些路径不过滤
        boolean shouldNotFilter = path.startsWith("/api/auth/") ||
                path.startsWith("/api/public/") ||
                path.startsWith("/api/debug/");

        if (shouldNotFilter) {
            logger.debug("跳过 JWT 过滤的路径: {}", path);
        }

        return shouldNotFilter;
    }
}