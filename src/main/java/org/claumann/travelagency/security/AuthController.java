package org.claumann.travelagency.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth/login")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody final AuthRequest authRequest) {
        var authentication = new UsernamePasswordAuthenticationToken(authRequest.username(), authRequest.password());
        var authenticate = authenticationManager.authenticate(authentication);

        var principal = (UserDetails) authenticate.getPrincipal();
        log.info("Generated token [{}]", tokenService.generateToken(principal));

        return ResponseEntity.ok(new AuthResponse(tokenService.generateToken(principal)));
    }

}
