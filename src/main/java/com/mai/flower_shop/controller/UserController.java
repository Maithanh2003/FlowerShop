package com.mai.flower_shop.controller;

import com.mai.flower_shop.dto.UserDto;
import com.mai.flower_shop.exception.AlreadyExistsException;
import com.mai.flower_shop.exception.ResourceNotFoundException;
import com.mai.flower_shop.model.User;
import com.mai.flower_shop.request.CreateUserRequest;
import com.mai.flower_shop.request.UpdateUserRequest;
import com.mai.flower_shop.response.ApiResponse;
import com.mai.flower_shop.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("${api.prefix}/users")
@RequiredArgsConstructor
public class UserController {
    private final IUserService userService;
    @GetMapping("/{userId}/user")
    public ResponseEntity <ApiResponse> getUserById(@PathVariable Long userId){
        try {
            User user = userService.getUserById(userId);
            UserDto userDto = userService.convertToDto(user);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(userDto)
                    .message("success").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(ApiResponse.builder()
                            .data(null)
                            .message(e.getMessage()).build());
        }
    }
    @PostMapping("/add")
    public ResponseEntity <ApiResponse> createUser(@RequestBody CreateUserRequest request){
        try {
            User user = userService.createUser(request);
            UserDto userDto = userService.convertToDto(user);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(userDto)
                    .message("success").build());
        } catch (AlreadyExistsException e) {
            return ResponseEntity
                    .status(CONFLICT)
                    .body(ApiResponse.builder()
                            .data(null)
                            .message(e.getMessage()).build());
        }
    }
    @PutMapping("/{userId}/update")
    public ResponseEntity <ApiResponse> updateUser(@RequestBody UpdateUserRequest request, @PathVariable Long userId){
        try {
            User user = userService.updateUser(request, userId);
            UserDto userDto = userService.convertToDto(user);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(userDto)
                    .message("update success").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(ApiResponse.builder()
                            .data(null)
                            .message(e.getMessage()).build());
        }
    }
    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<ApiResponse> deleteUser(@PathVariable Long userId) {
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(ApiResponse.builder()
                    .data(null)
                    .message("delete user success").build());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity
                    .status(NOT_FOUND)
                    .body(ApiResponse.builder()
                            .data(null)
                            .message(e.getMessage()).build());
        }
    }
}
