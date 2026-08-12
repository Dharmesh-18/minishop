package com.dharmesh.minishop.auth.service;

import com.dharmesh.minishop.auth.dto.LoginDTO;
import com.dharmesh.minishop.auth.dto.RegisterDTO;
import com.dharmesh.minishop.common.exception.BadRequestException;
import com.dharmesh.minishop.security.JwtTokenProvider;
import com.dharmesh.minishop.user.entity.Role;
import com.dharmesh.minishop.user.entity.User;
import com.dharmesh.minishop.user.repository.RoleRepository;
import com.dharmesh.minishop.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(LoginDTO loginDTO) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getUsername(),
                        loginDTO.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtTokenProvider.generateToken(authentication);
    }

    @Transactional
    public String register(RegisterDTO registerDTO) {
        if(userRepository.existsByUsername(registerDTO.getUsername())) {
            throw new BadRequestException("Username already taken!");
        }
        if(userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new BadRequestException("Email already taken!");
        }

        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("User role not set."));
        roles.add(userRole);

        if(registerDTO.isAdminRole()) {
            Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Admin role not set."));
            roles.add(adminRole);
        }

        User user = User.builder()
                .name(registerDTO.getName())
                .username(registerDTO.getUsername())
                .email(registerDTO.getEmail())
                .password(passwordEncoder.encode(registerDTO.getPassword()))
                .roles(roles)
                .build();

        userRepository.save(user);
        return "User registered successfully!";

    }

}
