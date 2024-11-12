package com.mai.flower_shop.controller;

import com.mai.flower_shop.exception.GlobalExceptionHandler;
import com.mai.flower_shop.model.InvalidatedToken;
import com.mai.flower_shop.repository.InvalidatedTokenRepository;
import com.mai.flower_shop.request.LoginRequest;
import com.mai.flower_shop.request.LogoutRequest;
import com.mai.flower_shop.request.RefreshRequest;
import com.mai.flower_shop.response.ApiResponse;
import com.mai.flower_shop.response.JwtResponse;
import com.mai.flower_shop.security.jwt.JwtUtils;
import com.mai.flower_shop.security.user.ShopUserDetails;
import com.mai.flower_shop.service.user.UserService;
import io.jsonwebtoken.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final InvalidatedTokenRepository invalidatedTokenRepository;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private final UserService userService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@Valid @RequestBody LoginRequest request) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            request.getEmail(), request.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            String jwt = jwtUtils.generateTokenForUser(authentication, true);
            ShopUserDetails userDetails = (ShopUserDetails) authentication.getPrincipal();
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), jwt);
            return ResponseEntity.ok(ApiResponse.builder().message("login success").data(jwtResponse).build());
        } catch (AuthenticationException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.builder().message(e.getMessage()).data(null).build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder().message("An unexpected error occurred").data(null).build());
        }
    }
    @Transactional
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@RequestBody LogoutRequest request) {
        try {
            String token = request.getToken();
            String jwtId = jwtUtils.getIDFromToken(token);
            InvalidatedToken invalidatedToken = new InvalidatedToken(jwtId);

            // Save the invalidated token in the database
            invalidatedTokenRepository.save(invalidatedToken);
            SecurityContextHolder.clearContext();

            return ResponseEntity.ok(ApiResponse.builder().message("Logout successful").data(null).build());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder().message("An error occurred during logout").data(null).build());
        }
    }
    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refresh(@RequestBody RefreshRequest request) {
        try {
            String token = request.getToken();
            if (!jwtUtils.validateToken(token)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder().message("Invalid or expired refresh token").data(null).build());
            }
            String jwtId = jwtUtils.getIDFromToken(token);
            if (invalidatedTokenRepository.existsById(jwtId)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(ApiResponse.builder().message("Token has been invalidated").data(null).build());
            }
            InvalidatedToken invalidatedToken = new InvalidatedToken(jwtId);
            invalidatedTokenRepository.save(invalidatedToken);
            String username = jwtUtils.getUsernameFromToken(token);
            ShopUserDetails userDetails = (ShopUserDetails) userDetailsService.loadUserByUsername(username);
            String newAccessToken = jwtUtils.generateTokenForUser(new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities()), false);

            // Trả về JWT mới cho người dùng
            JwtResponse jwtResponse = new JwtResponse(userDetails.getId(), newAccessToken);
            return ResponseEntity.ok(ApiResponse.builder().message("Token refreshed successfully").data(jwtResponse).build());

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.builder().message("An error occurred while refreshing token").data(null).build());
        }
    }
    @PostMapping("/verify")
    public ResponseEntity<ApiResponse> verifyEmail(@RequestParam("token") String token) {
        boolean isVerified = userService.verifyUser(token);
        if (isVerified) {
            return ResponseEntity.ok(ApiResponse.builder().message("Email verified successfully").build());
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.builder().message("Invalid token").build());
    }
}