package com.ptit.event_management.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleDTO {
    @NotBlank
    Long eventId;
    @NotBlank
    String title;
    @NotBlank
    String description;
    List<String> images;
    @NotNull
    Timestamp startedAt;
    @NotNull
    Timestamp endedAt;
}
