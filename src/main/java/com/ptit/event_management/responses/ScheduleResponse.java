package com.ptit.event_management.responses;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ptit.event_management.models.Event;
import com.ptit.event_management.models.Schedule;
import com.ptit.event_management.models.ScheduleImage;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ScheduleResponse {
    Long id;
    EventResponse event;
    String title;
    String description;
    Timestamp startedAt;
    Timestamp endedAt;
    List<String> images;
    public static ScheduleResponse fromSchedule(Schedule schedule) {
        if (schedule == null) return null;

        List<String> images = schedule.getImages() != null
                ? schedule.getImages().stream()
                .map(ScheduleImage::getUrl)
                .collect(Collectors.toList())
                : List.of();

        return ScheduleResponse.builder()
                .id(schedule.getId())
                .event(EventResponse.fromEvent(schedule.getEvent()))
                .title(schedule.getTitle())
                .description(schedule.getDescription())
                .startedAt(schedule.getStartedAt())
                .endedAt(schedule.getEndedAt())
                .images(images)
                .build();
    }
}
