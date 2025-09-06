package com.ptit.event_management.services.auth;

import com.ptit.event_management.dtos.AuthDTO;
import com.ptit.event_management.models.User;
import jakarta.mail.MessagingException;

public interface AuthService {
    User register(AuthDTO authDTO) throws MessagingException;
    String login(AuthDTO authDTO) throws Exception;
    User verifyOtp(String email, String otp);
    User getUserFromToken(String token);
}
