package com.ptit.event_management.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ptit.event_management.models.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventResponse {
    Long id;
    User owner;
    String name;
    String avatar;
    List<EventImage> images;
    String description;
    EventStatus status;
    Timestamp startedAt;
    Timestamp endedAt;
    Location location;
    Timestamp createdAt;
    Timestamp updatedAt;
}
