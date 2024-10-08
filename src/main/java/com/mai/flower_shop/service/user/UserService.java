package com.mai.flower_shop.service.user;

import com.mai.flower_shop.dto.UserDto;
import com.mai.flower_shop.exception.AlreadyExistsException;
import com.mai.flower_shop.exception.ResourceNotFoundException;
import com.mai.flower_shop.model.User;
import com.mai.flower_shop.repository.UserRepository;
import com.mai.flower_shop.request.CreateUserRequest;
import com.mai.flower_shop.request.UpdateUserRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService{
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                ()-> new ResourceNotFoundException("user not found")
        );
    }

    @Override
    public User createUser(CreateUserRequest request) {
        return Optional.of(request)
                .filter(user -> !userRepository.existsByEmail(request.getEmail()))
                .map(req -> {
                    User user = new User();
                    user.setFirstName(req.getFirstName());
                    user.setLastName(req.getLastName());
                    user.setEmail(req.getEmail());
                    user.setPassword(req.getPassword());
                    return  userRepository.save(user);
                }).orElseThrow( () -> new AlreadyExistsException(request.getEmail() + "user already exits"));
    }

    @Override
    public User updateUser(UpdateUserRequest request, Long userId) {
        return userRepository
                .findById(userId)
                .map(existingUser -> {
                    existingUser.setFirstName(request.getFirstName());
                    existingUser.setLastName(request.getLastName());
                    return userRepository.save(existingUser);
                }).orElseThrow( () -> new ResourceNotFoundException("user not found")
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
    public UserDto convertToDto(User user){
        return modelMapper.map(user, UserDto.class);
    }
}
