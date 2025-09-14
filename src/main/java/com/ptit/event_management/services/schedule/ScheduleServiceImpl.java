package com.ptit.event_management.services.schedule;

import com.ptit.event_management.dtos.ScheduleDTO;
import com.ptit.event_management.models.Event;
import com.ptit.event_management.models.Schedule;
import com.ptit.event_management.models.ScheduleImage;
import com.ptit.event_management.models.User;
import com.ptit.event_management.repositories.EventRepository;
import com.ptit.event_management.repositories.ScheduleRepository;
import com.ptit.event_management.services.user.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService{
    private final ScheduleRepository scheduleRepository;
    private final EventRepository eventRepository;
    private final UserService userService;
    @Override
    public Schedule addSchedule(ScheduleDTO dto, Long reqUserId) {
        Event event = eventRepository.findById(dto.getEventId())
                .orElseThrow(() -> new EntityNotFoundException("Event not found"));

        // check quyền: chỉ owner mới thêm được schedule
        if (!event.getOwner().getId().equals(reqUserId)) {
            throw new RuntimeException("Permission denied");
        }

        Schedule schedule = Schedule.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startedAt(dto.getStartedAt())
                .endedAt(dto.getEndedAt())
                .event(event)
                .build();
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            List<ScheduleImage> images = dto.getImages().stream()
                    .map(url -> ScheduleImage.builder()
                            .url(url)
                            .schedule(schedule)
                            .build())
                    .collect(Collectors.toList());
            schedule.setImages(images);
        }
        return scheduleRepository.save(schedule);
    }

    @Override
    public Schedule updateSchedule(Long id, ScheduleDTO dto, Long reqUserId) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));

        if (!schedule.getEvent().getOwner().getId().equals(reqUserId)) {
            throw new RuntimeException("Permission denied");
        }

        schedule.setTitle(dto.getTitle());
        schedule.setDescription(dto.getDescription());
        schedule.setStartedAt(dto.getStartedAt());
        schedule.setEndedAt(dto.getEndedAt());
        schedule.getImages().clear();
        if (dto.getImages() != null && !dto.getImages().isEmpty()) {
            List<ScheduleImage> images = dto.getImages().stream()
                    .map(url -> ScheduleImage.builder()
                            .url(url)
                            .schedule(schedule)
                            .build())
                    .toList();
            schedule.getImages().addAll(images);
        }
        return scheduleRepository.save(schedule);
    }

    @Override
    public Page<Schedule> findByEventId(Long eventId, Pageable pageable) {
        return scheduleRepository.findByEventId(eventId, pageable);
    }

    @Override
    public Schedule getScheduleDetail(Long id) {
        return scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
    }

    @Override
    public void deleteSchedule(Long id, Long reqUserId) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Schedule not found"));
        if (!schedule.getEvent().getOwner().getId().equals(reqUserId)) {
            throw new RuntimeException("Permission denied");
        }
        schedule.setDeleted(true);
        scheduleRepository.save(schedule);
    }
}
