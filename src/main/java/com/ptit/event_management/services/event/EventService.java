package com.ptit.event_management.services.event;

import com.ptit.event_management.dtos.EventDTO;
import com.ptit.event_management.dtos.EventFilterDTO;
import com.ptit.event_management.models.Event;
import com.ptit.event_management.models.EventInvitation;
import com.ptit.event_management.models.EventStatus;
import com.ptit.event_management.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;

public interface EventService {
    Event createEvent(EventDTO dto, Long userId);
    Event updateEvent(Long id, EventDTO dto, Long reqUserId);
    Event getEventDetail(Long id);
    void deleteEvent(Long id, Long reqUserId);
    Page<Event> getAll(Pageable pageable);
//    Page<Event> filter(EventFilterDTO filter, Pageable pageable);
    Event changStatus(Long id, EventStatus state);
    void inviteUserToEvent(Long eventId, ArrayList<Long> userIds, User reqUser);
    void acceptInvitation(Long eventId, User reqUser);
    public void declineInvitation(Long eventId, User reqUser);
    Page<User> getGuestsByEventId(Long eventId, Pageable pageable);
    Event setLocation(Long eventId, Long locationId, Long userId);
    Page<Event> getEventByUser(Long userId, Pageable pageable);
    Page<EventInvitation> getInvitationList(Long userId, Pageable pageable);
}
