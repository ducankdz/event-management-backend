package com.ptit.event_management.responses;

import com.ptit.event_management.models.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventResponse {
    Long id;
    UserResponse owner;
    String name;
    String avatar;
    List<String> images;
    String description;
    EventStatus status;
    Timestamp startedAt;
    Timestamp endedAt;
    Location location;
    Timestamp createdAt;
    Timestamp updatedAt;

    public static EventResponse fromEvent(Event event) {
        if (event == null) {
            return null;
        }
        return EventResponse.builder()
                .id(event.getId())
                .name(event.getName())
                .avatar(event.getAvatar())
                .owner(UserResponse.fromUser(event.getOwner()))
                .images(
                    Optional.ofNullable(event.getImages())
                        .orElse(List.of())
                        .stream()
                        .map(EventImage::getUrl)
                        .collect(Collectors.toList())
                )
                .description(event.getDescription())
                .status(event.getStatus())
                .startedAt(event.getStartedAt())
                .endedAt(event.getEndedAt())
                .location(event.getLocation())
                .createdAt(event.getCreatedAt())
                .updatedAt(event.getUpdatedAt())
                .build();
    }
}
