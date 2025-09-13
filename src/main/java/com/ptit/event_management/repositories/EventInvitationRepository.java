package com.ptit.event_management.repositories;

import com.ptit.event_management.models.EventInvitation;
import com.ptit.event_management.models.InvitationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EventInvitationRepository extends JpaRepository<EventInvitation, Long> {
    Optional<EventInvitation> findByEventIdAndUserId(Long eventId, Long userId);
    Page<EventInvitation> findByEventIdAndStatus(Long eventId, InvitationStatus status, Pageable pageable);
    Page<EventInvitation> findByUserIdAndStatus(Long userId, InvitationStatus status, Pageable pageable);
}

