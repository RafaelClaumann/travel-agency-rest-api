package org.claumann.travelagency.security;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth/login")
public class AuthController {

    private final LoginUseCaseImpl loginUseCase;

    public AuthController(LoginUseCaseImpl loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody final AuthRequest authRequest) {
        var authResult = loginUseCase.execute(authRequest);
        return ResponseEntity.ok(authResult);
    }

}
