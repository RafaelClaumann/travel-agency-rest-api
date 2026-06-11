package org.claumann.travelagency.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (jwtToken == null || !jwtToken.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Found token in header [{}]", jwtToken);
        filterChain.doFilter(request, response);
    }

}
