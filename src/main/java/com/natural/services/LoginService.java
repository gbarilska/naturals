package com.natural.services;

import com.natural.dtos.UserDetailsImpl;
import com.natural.entities.User;
import com.natural.handlers.GlobalExceptionHandler;
import com.natural.repositories.UserRepository;
import com.natural.requests.AuthenticationRequest;
import com.natural.responses.AuthenticationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.natural.utils.JwtUtil.generateToken;

@Service
public class LoginService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginService.class);

    private final AuthenticationManager authenticationManager;

    private UserDetailsService userDetailsService;

    private UserRepository userRepository;

    @Autowired
    public LoginService(AuthenticationManager authenticationManager, UserDetailsService userDetailsService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    public AuthenticationResponse authenticateUser(AuthenticationRequest request) {
       Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow(() -> new UsernameNotFoundException("Email not found " + authentication.getName()));

        String jwt = generateToken(authentication.getName());

        return new AuthenticationResponse(jwt,user);
    }
}
