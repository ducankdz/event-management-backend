package com.ptit.event_management.dtos;

import com.ptit.event_management.models.EventStatus;

import java.sql.Timestamp;

public class EventFilterDTO {
    private String name;
    private EventStatus state;
    private Timestamp startedAt;
    private Timestamp endedAt;
}
