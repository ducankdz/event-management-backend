package com.ptit.event_management.services.event;

import com.ptit.event_management.dtos.EventDTO;
import com.ptit.event_management.dtos.EventFilterDTO;
import com.ptit.event_management.models.*;
import com.ptit.event_management.repositories.EventInvitationRepository;
import com.ptit.event_management.repositories.EventRepository;
import com.ptit.event_management.repositories.LocationRepository;
import com.ptit.event_management.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService{
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final LocationRepository locationRepository;
    private final EventInvitationRepository invitationRepository;
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

    @Override
    public Page<Event> getAll(Pageable pageable) {
        return eventRepository.findAll(pageable);
    }


    @Override
    public Event changStatus(Long id, EventStatus status) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        event.setStatus(status);
        return eventRepository.save(event);
    }

    @Override
    public void inviteUserToEvent(Long eventId, ArrayList<Long> userIds, User reqUser) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        // chỉ cho phép chủ event mời
        if (!event.getOwner().getId().equals(reqUser.getId())) {
            throw new RuntimeException("Only event owner can invite users");
        }

        for (Long userId : userIds) {
            User guest = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

            // check xem đã tồn tại lời mời chưa
            Optional<EventInvitation> existing = invitationRepository.findByEventIdAndUserId(eventId, userId);
            if (existing.isPresent()) {
                continue; // bỏ qua nếu đã mời rồi
            }

            EventInvitation invitation = EventInvitation.builder()
                    .event(event)
                    .user(guest)
                    .inviter(reqUser)
                    .status(InvitationStatus.INVITED)
                    .build();
            invitationRepository.save(invitation);
        }
    }

    @Override
    public void acceptInvitation(Long eventId, User reqUser) {
        EventInvitation invitation = invitationRepository.findByEventIdAndUserId(eventId, reqUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found"));

        if (invitation.getStatus() != InvitationStatus.INVITED) {
            throw new RuntimeException("Invitation already responded");
        }

        invitation.setStatus(InvitationStatus.ACCEPTED);
        invitationRepository.save(invitation);
    }

    @Override
    public void declineInvitation(Long eventId, User reqUser) {
        EventInvitation invitation = invitationRepository.findByEventIdAndUserId(eventId, reqUser.getId())
                .orElseThrow(() -> new EntityNotFoundException("Invitation not found"));

        if (invitation.getStatus() != InvitationStatus.INVITED) {
            throw new RuntimeException("Invitation already responded");
        }

        invitation.setStatus(InvitationStatus.DECLINED);
        invitationRepository.save(invitation);
    }

    @Override
    public Page<User> getGuestsByEventId(Long eventId, Pageable pageable) {
        Page<EventInvitation> invitations = invitationRepository.findByEventIdAndStatus(
                eventId, InvitationStatus.ACCEPTED, pageable
        );
        return invitations.map(EventInvitation::getUser);
    }

    @Override
    public Event setLocation(Long eventId, Long locationId, Long userId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));
        if (!event.getOwner().getId().equals(userId)) {
            throw new RuntimeException("Permission denied");
        }
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new EntityNotFoundException("Location not found"));
        event.setLocation(location);
        return eventRepository.save(event);
    }

    @Override
    public Page<Event> getEventByUser(Long userId, Pageable pageable) {
        return eventRepository.findByOwnerId(userId, pageable);
    }

    @Override
    public Page<EventInvitation> getInvitationList(Long userId, Pageable pageable) {
        return invitationRepository.findByUserIdAndStatus(userId,InvitationStatus.INVITED, pageable);
    }

}
