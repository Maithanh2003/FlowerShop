package com.mai.flower_shop.security.user;

import com.mai.flower_shop.exception.ResourceNotFoundException;
import com.mai.flower_shop.model.User;
import com.mai.flower_shop.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShopUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = Optional.ofNullable(userRepository.findByEmail(email))
                .orElseThrow( () -> new ResourceNotFoundException("User not found"));
        if (!user.isEnabled()) {
            throw new DisabledException("Account is not verified");
        }
        return ShopUserDetails.buildUserDetails(user);
    }
}
