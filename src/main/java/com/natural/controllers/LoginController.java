package com.natural.controllers;

import com.natural.entities.User;
import com.natural.requests.AuthenticationRequest;
import com.natural.responses.AuthenticationResponse;
import com.natural.services.LoginService;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class LoginController {

    private final LoginService loginService;

    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }


    @GetMapping("/")
    public String getIndexPage() {
        return "Hello";
    }

    @GetMapping("user")
    public String getUserPage() {
        return "Hello";
    }

    @GetMapping("admin")
    public String getAdminPage() {
        return "Hello";
    }

    @PostMapping("login")
    public ResponseEntity<User> createAuthenticationTokes(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        AuthenticationResponse response = loginService.authenticateUser(authenticationRequest);

        String jwtCookie = ResponseCookie.from("access", response.getJwt())
                .httpOnly(true)
                .build()
                .toString();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie)
                .body(response.getUser());
    }
}
