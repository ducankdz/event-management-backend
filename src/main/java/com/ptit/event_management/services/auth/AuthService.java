package com.ptit.event_management.services.auth;

import com.ptit.event_management.dtos.AuthDTO;
import com.ptit.event_management.models.User;

public interface AuthService {
    User register(AuthDTO authDTO);
    String login(AuthDTO authDTO);
    User verifyOtp(String email, String otp);
}
