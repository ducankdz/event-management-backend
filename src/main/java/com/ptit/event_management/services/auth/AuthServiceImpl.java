package com.ptit.event_management.services.auth;

import com.ptit.event_management.configurations.JwtTokenUtil;
import com.ptit.event_management.dtos.AuthDTO;
import com.ptit.event_management.models.Otp;
import com.ptit.event_management.models.Role;
import com.ptit.event_management.models.User;
import com.ptit.event_management.repositories.OtpRepository;
import com.ptit.event_management.repositories.RoleRepository;
import com.ptit.event_management.repositories.UserRepository;
import com.ptit.event_management.services.email.EmailService;
import com.ptit.event_management.services.user.CustomUserDetailsServiceImpl;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;
    private final CustomUserDetailsServiceImpl customUserDetailsService;
    private final RoleRepository roleRepository;
    private final OtpRepository otpRepository;
    private final EmailService emailService;

    @Override
    public User register(AuthDTO authDTO) throws MessagingException {
        String email = authDTO.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        if (!authDTO.getPassword().equals(authDTO.getRetypePassword())) {
            throw new RuntimeException("Password doesn't match");
        }
        if (authDTO.getRoleId() == null) {
            authDTO.setRoleId(1L);
        }
        Role role =
                roleRepository
                        .findById(authDTO.getRoleId())
                        .orElseThrow(() -> new RuntimeException("Role doesn't exist"));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        User newUser =
                User.builder()
                        .email(authDTO.getEmail())
                        .password(passwordEncoder.encode(authDTO.getPassword()))
                        .fullName(authDTO.getFullName())
                        .createdAt(Timestamp.valueOf(LocalDateTime.now())).phone(authDTO.getPhone())
                        .roles(roles)
                        .phone(authDTO.getPhone())
                        .isVerified(false)
                        .build();
        User savedUser = userRepository.save(newUser);

        String otpCode = emailService.generateOtp();
        LocalDateTime otpExpiration = LocalDateTime.now().plusMinutes(2);
        Otp otp = Otp.builder()
                .otp(otpCode)
                .expirationTime(Timestamp.valueOf(otpExpiration))
                .user(savedUser)
                .build();
        otpRepository.save(otp);

        // Send OTP Mail
        emailService.sendOtpMail(email, otpCode);

        return savedUser;
    }

    @Override
    public String login(AuthDTO authDTO) throws Exception {
        String email = authDTO.getEmail();
        String password = authDTO.getPassword();
        User user =
                userRepository
                        .findByEmail(email)
                        .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        if (!user.isVerified()) {
            throw new RuntimeException(
                    "Your email has not been verified. Please verify your email before logging in.");
        }
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid email or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Invalid email or password");
        }
        Authentication auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
        return jwtTokenUtil.generateToken(auth);
    }

    @Override
    public User verifyOtp(String email, String otpCode) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));

        // Lấy OTP theo user và mã OTP
        Otp otp = otpRepository.findTopByUserAndOtpOrderByExpirationTimeDesc(user, otpCode)
                .orElseThrow(() -> new RuntimeException("Invalid OTP"));

        // Kiểm tra thời hạn OTP
        if (otp.getExpirationTime().before(Timestamp.valueOf(LocalDateTime.now()))) {
            throw new RuntimeException("OTP has expired");
        }

        // Nếu hợp lệ thì xác minh user
        user.setVerified(true);
        userRepository.save(user);

//        otpRepository.delete(otp);
        return user;
    }

    @Override
    public User getUserFromToken(String token) {
        String email = jwtTokenUtil.getEmailFromToken(token);
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email"));
    }

}
