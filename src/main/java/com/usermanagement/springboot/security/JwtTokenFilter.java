package com.usermanagement.springboot.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.usermanagement.springboot.common.Constants.AUTHORIZATION;
import static com.usermanagement.springboot.common.Constants.BEARER;

/**
 * Here, this custom filter class extends the @OncePerRequestFilter class to guarantee a single execution per
 * request.
 * When it comes into play, the @doFilterInternal() method gets invoked. Here’s how it works:
 * If the Authorization header of the request doesn’t contain a Bearer token, it continues the
 * filter chain without updating authentication context.
 * Else, if the token is not verified, continue the filter chain without updating authentication context.
 * If the token is verified, update the authentication context with the user details.
 * In other words, it tells Spring that the user is authenticated, and continue the downstream filters.
 */
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    @Autowired
    private JwtTokenUtil jwtUtil;
    @Autowired
    private CustomUserDetailsService service;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader(AUTHORIZATION);
        String token = null;
        String userName = null;

        if (authorizationHeader != null && authorizationHeader.startsWith(BEARER)) {

            token = authorizationHeader.substring(7);
            userName = jwtUtil.extractUsername(token);
        }
        if (userName != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = service.loadUserByUsername(userName);
            if (jwtUtil.validateToken(token, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                usernamePasswordAuthenticationToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
