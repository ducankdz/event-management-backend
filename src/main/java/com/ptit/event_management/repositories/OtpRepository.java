package com.ptit.event_management.repositories;

import com.ptit.event_management.models.Otp;
import com.ptit.event_management.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OtpRepository extends JpaRepository<Otp, Long> {
    Optional<Otp> findTopByUserAndOtpOrderByExpirationTimeDesc(User user, String otp);
}
