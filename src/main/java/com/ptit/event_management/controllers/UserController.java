package com.ptit.event_management.controllers;

import com.ptit.event_management.dtos.UserDTO;
import com.ptit.event_management.models.User;
import com.ptit.event_management.responses.MessageResponse;
import com.ptit.event_management.responses.UserResponse;
import com.ptit.event_management.services.auth.AuthService;
import com.ptit.event_management.services.user.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthService authService;
    @GetMapping("/profile")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "Lấy thông tin người dùng đang đăng nhập",
            description = "Yêu cầu token hợp lệ trong header Authorization để lấy thông tin profile của người dùng hiện tại."
    )
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String jwt){
        try {
            User user = authService.getUserFromToken(jwt);
            UserResponse userResponse = UserResponse.fromUser(user);
            return ResponseEntity.ok(userResponse);
        }
        catch (Exception e){
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "Lấy thông tin người dùng theo ID",
            description = "Yêu cầu token hợp lệ trong header Authorization để lấy thông tin chi tiết của người dùng theo ID."
    )
    public ResponseEntity<?> getUserById(@RequestHeader("Authorization") String jwt,
                                         @PathVariable("id") Long id){
        try {
            User reqUser = authService.getUserFromToken(jwt);
            User user = userService.findUserById(id);
            UserResponse userResponse = UserResponse.fromUser(user);
            return ResponseEntity.ok(userResponse);
        }
        catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    @GetMapping()
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "Lấy danh sách người dùng đã xác minh",
            description = "Trả về danh sách tất cả người dùng mà đã xác thực tài khoản (isVerified = true)."
    )
    public ResponseEntity<?> getAllVerifiedUsers(@RequestHeader("Authorization") String jwt,
                                                 Pageable pageable) {
        User reqUser = authService.getUserFromToken(jwt);
        Page<User> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }
    @PutMapping("/profile/update")
    @PreAuthorize("isAuthenticated()")
    @Operation(
            summary = "Cập nhật thông tin người dùng",
            description = "Cập nhật các thông tin cá nhân của người dùng đang đăng nhập như tên đầy đủ, số điện thoại và ảnh đại diện."
    )
    public ResponseEntity<User> updateUser(@RequestBody UserDTO userDTO,
                                           @RequestHeader("Authorization") String jwt) {
        try {
            User reqUser = authService.getUserFromToken(jwt);
            User user = userService.updateUser(reqUser.getId(), userDTO);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
