package com.ptit.event_management.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "event_invitations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"event_id", "user_id"})) // ✅ tránh mời 1 user nhiều lần cho 1 event
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventInvitation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    // Người được mời
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    User user;

    // Sự kiện liên quan
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    @JsonIgnore
    Event event;

    // Trạng thái lời mời (PENDING / ACCEPTED / DECLINED)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    InvitationStatus status;

    // Người gửi lời mời
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inviter_id", nullable = false)
    @JsonIgnore
    User inviter;
}
