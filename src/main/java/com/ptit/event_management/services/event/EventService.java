package com.ptit.event_management.services.event;

import com.ptit.event_management.dtos.EventDTO;
import com.ptit.event_management.models.Event;
import com.ptit.event_management.models.User;

public interface EventService {
    Event createEvent(EventDTO dto, Long userId);
    Event updateEvent(Long id, EventDTO dto, Long reqUserId);
    Event getEventDetail(Long id);
    void deleteEvent(Long id, Long reqUserId);
}
