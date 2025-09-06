package com.ptit.event_management.controllers;

import com.ptit.event_management.dtos.AuthDTO;
import com.ptit.event_management.models.User;
import com.ptit.event_management.responses.AuthResponse;
import com.ptit.event_management.responses.MessageResponse;
import com.ptit.event_management.responses.UserResponse;
import com.ptit.event_management.services.auth.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    @Operation(
            summary = "Tạo mới người dùng",
            description = "API này sẽ nhận vào thông tin người dùng và lưu vào database."
    )
    public ResponseEntity<?> createAccount(@RequestBody AuthDTO authDTO) {
        try {
            User user = authService.register(authDTO);
            UserResponse userResponse = UserResponse.fromUser(user);
            return new ResponseEntity<>(
                    userResponse,
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.OK
            );
        }
    }

    @PostMapping("/login")
    @Operation(
            summary = "Đăng nhập người dùng",
            description = "API này sẽ nhận thông tin đăng nhập (email và mật khẩu) và trả về JWT token nếu đăng nhập thành công."
    )
    public ResponseEntity<?> login(@RequestBody AuthDTO authDTO) {
        try {
            String jwt = authService.login(authDTO);
            return new ResponseEntity<>(
                    AuthResponse.builder()
                            .jwt(jwt)
                            .message("Login successfully")
                            .build(),
                    HttpStatus.CREATED
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    AuthResponse.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.OK
            );
        }
    }
    @PostMapping("/otp/verify")
    @Operation(
            summary = "Xác thực OTP",
            description = "API này sẽ nhận email và mã OTP để xác thực người dùng."
    )
    public ResponseEntity<?> verifyOtp(@RequestParam String email,
                                       @RequestParam String otp) {
        try {
            User user = authService.verifyOtp(email, otp);
            UserResponse userResponse = UserResponse.fromUser(user);
            return ResponseEntity.ok(userResponse);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.OK
            );
        }
    }
}
