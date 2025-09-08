package com.ptit.event_management.services.event;

import com.ptit.event_management.dtos.EventDTO;
import com.ptit.event_management.mappers.EventMapper;
import com.ptit.event_management.models.Event;
import com.ptit.event_management.models.EventImage;
import com.ptit.event_management.models.EventStatus;
import com.ptit.event_management.models.User;
import com.ptit.event_management.repositories.EventRepository;
import com.ptit.event_management.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

        Event event = Event.builder()
                .name(dto.getName())
                .avatar(dto.getAvatar())
                .description(dto.getDescription())
                .startedAt(dto.getStartedAt())
                .endedAt(dto.getEndedAt())
                .budget(dto.getBudget())
                .status(EventStatus.NOT_STARTED)
                .owner(user)
                .build();
        List<EventImage> eventImages = Optional.ofNullable(dto.getImages())
                .orElse(List.of())
                .stream()
                .map(img -> EventImage.builder()
                        .url(img)
                        .event(event)
                        .build())
                .toList();

        event.setImages(eventImages);

        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Long id, EventDTO dto, Long reqUserId) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));

        if (!event.getOwner().getId().equals(reqUserId)) {
            throw new SecurityException("You are not allowed to update this event");
        }

        event.setName(dto.getName());
        event.setAvatar(dto.getAvatar());
        event.setDescription(dto.getDescription());
        event.setStartedAt(dto.getStartedAt());
        event.setEndedAt(dto.getEndedAt());
        event.setBudget(dto.getBudget());

        event.getImages().clear();
        List<EventImage> newImages = Optional.ofNullable(dto.getImages())
                .orElse(List.of())
                .stream()
                .map(img -> EventImage.builder()
                        .url(img)
                        .event(event)
                        .build())
                .toList();
        event.getImages().addAll(newImages);

        return eventRepository.save(event);
    }

    @Override
    public Event getEventDetail(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));
    }

    @Override
    public void deleteEvent(Long id, Long reqUserId) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + id));
        if (!event.getOwner().getId().equals(reqUserId)) {
            throw new SecurityException("You are not allowed to delete this event");
        }
        // Xóa mềm: set cờ isDeleted = true
        event.setDeleted(true);
        eventRepository.save(event);

        // eventRepository.delete(event);
    }

}
