//package org.shop.security;
//
//import jakarta.servlet.FilterChain;
//import jakarta.servlet.ServletException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import org.shop.models.user.User;
//import org.shop.services.jwt.JWTService;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
//import org.springframework.stereotype.Component;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import java.io.IOException;
//import java.util.Collections;
//
//@Component
//public class JWTFilter extends OncePerRequestFilter {
//
//    private final JWTService jwtService;
//
//    public JWTFilter(JWTService jwtService) {
//        this.jwtService = jwtService;
//    }
//
//    @Override
//    protected void doFilterInternal(
//            HttpServletRequest request,
//            HttpServletResponse response,
//            FilterChain filterChain
//    ) throws ServletException, IOException {
//
//        final String authHeader = request.getHeader("Authorization");
//
//        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        final String token = authHeader.substring(7); // "Bearer ".length() == 7
//
//        try {
//            String username = jwtService.extractUsername(token);
//            String role = jwtService.extractRole(token);
//
//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                User user = User.builder()
//                        .login(username)
//                        .roles(role)
//                        .build();
//
//                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                        user,
//                        null,
//                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role)) // rola np. "ROLE_admin"
//                );
//
//                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                SecurityContextHolder.getContext().setAuthentication(authToken);
//            }
//
//        } catch (Exception e) {
//            System.out.println("JWT error: " + e.getMessage());
//        }
//
//        filterChain.doFilter(request, response);
//    }
//}
//
