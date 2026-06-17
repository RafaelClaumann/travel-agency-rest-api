package org.claumann.travelagency.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class LoginUseCaseImpl {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public LoginUseCaseImpl(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public AuthResponse execute(AuthRequest request) {

        var userPassAuth = new UsernamePasswordAuthenticationToken(request.username(), request.password());
        var authentication = authenticationManager.authenticate(userPassAuth);

        var user = (UserDetails) authentication.getPrincipal();
        var token = tokenService.generateToken(user);

        return new AuthResponse(token);
    }

}
