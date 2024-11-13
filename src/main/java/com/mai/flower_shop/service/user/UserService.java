package com.mai.flower_shop.service.user;

import com.mai.flower_shop.data.RoleRepository;
import com.mai.flower_shop.dto.UserDto;
import com.mai.flower_shop.exception.AlreadyExistsException;
import com.mai.flower_shop.exception.ResourceNotFoundException;
import com.mai.flower_shop.model.Role;
import com.mai.flower_shop.model.User;
import com.mai.flower_shop.model.VerificationToken;
import com.mai.flower_shop.repository.UserRepository;
import com.mai.flower_shop.repository.VerificationTokenRepository;
import com.mai.flower_shop.request.CreateUserRequest;
import com.mai.flower_shop.request.UpdateUserRequest;
import com.mai.flower_shop.service.email.EmailService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final VerificationTokenRepository tokenRepository;
    private static final Random RANDOM = new SecureRandom();

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("user not found")
        );
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    Role userRole = roleRepository.findByName("ROLE_USER").get();
                    User user = new User();
                    user.setFirstName(req.getFirstName());
                    user.setLastName(req.getLastName());
                    user.setEmail(req.getEmail());
                    user.setPassword(passwordEncoder.encode(req.getPassword()));
                    user.setRoles(Set.of(userRole));
                    user.setEnabled(false);
                    User savedUser = userRepository.save(user);

                    String otpCode = generateOtpCode();
                    VerificationToken verificationCode = new VerificationToken(otpCode, user);
                    tokenRepository.save(verificationCode);
                    try {
                        emailService.sendVerificationEmail(user.getEmail(), otpCode);
                    } catch (MessagingException e) {
                        e.printStackTrace();
                    }

                    return savedUser;
                }).orElseThrow(() -> new AlreadyExistsException(request.getEmail() + "user already exits"));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return userRepository
                .findById(userId)
                .map(existingUser -> {
                    existingUser.setFirstName(request.getFirstName());
                    existingUser.setLastName(request.getLastName());
                    return userRepository.save(existingUser);
                }).orElseThrow(() -> new ResourceNotFoundException("user not found")
                );
    }

    @Override
    public void deleteUser(Long userId) {
        userRepository.findById(userId)
                .ifPresentOrElse(userRepository::delete, () -> {
                    throw new ResourceNotFoundException("user not found");
                });
    }

    @Override
    public UserDto convertToDto(User user) {
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email);
    }

    public boolean verifyUser(String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid token"));
        if (verificationToken.getExpiryDate().before(new Date())) {
            throw new RuntimeException("Verification code has expired");
        }
        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        return true;
    }

    private String generateOtpCode() {
        return String.format("%04d", RANDOM.nextInt(10000));
    }
}
