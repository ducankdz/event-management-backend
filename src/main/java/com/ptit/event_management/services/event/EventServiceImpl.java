package com.ptit.event_management.services.event;

import com.ptit.event_management.dtos.EventDTO;
import com.ptit.event_management.mappers.EventMapper;
import com.ptit.event_management.models.Event;
import com.ptit.event_management.models.EventStatus;
import com.ptit.event_management.models.User;
import com.ptit.event_management.repositories.EventRepository;
import com.ptit.event_management.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EventMapper eventMapper;
    @Override
    public Event createEvent(EventDTO dto, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        Event event = eventMapper.toEntity(dto);
        event.setOwner(user);
        event.setStatus(EventStatus.NOT_STARTED);

        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Long id, EventDTO dto) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));

        eventMapper.updateEventFromDto(dto, existingEvent);

        return eventRepository.save(existingEvent);
    }

    @Override
    public Event getEventDetail(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));
    }

    @Override
    public void deleteEvent(Long id) {
        Event existingEvent = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));
        existingEvent.setDeleted(true);
    }
}
