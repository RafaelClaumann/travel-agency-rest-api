package org.claumann.travelagency.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {


    private final UserDetailsServiceImpl userDetailsService;
    private final TokenService tokenService;

    public JwtAuthenticationFilter(UserDetailsServiceImpl userDetailsService, TokenService tokenService) {
        this.userDetailsService = userDetailsService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String jwtToken = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (jwtToken == null || !jwtToken.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtToken.substring(7); // remove "Bearer "
        String username = tokenService.validateToken(token);
        if (username != null) {
            UserDetails user = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        log.info("Found token in header [{}]", jwtToken);
        filterChain.doFilter(request, response);
    }

}
