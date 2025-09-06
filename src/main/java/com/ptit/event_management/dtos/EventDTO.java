package com.ptit.event_management.dtos;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDTO {
    String name;
    String avatar;
//    List<String> images;
    Timestamp startedAt;
    Timestamp endedAt;
    String description;
    Long budget;
}
