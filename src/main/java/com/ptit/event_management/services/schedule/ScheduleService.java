package com.ptit.event_management.services.schedule;

import com.ptit.event_management.dtos.ScheduleDTO;
import com.ptit.event_management.models.Schedule;
import com.ptit.event_management.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ScheduleService {
    Schedule addSchedule(ScheduleDTO dto, Long reqUserId);

    Schedule updateSchedule(Long id, ScheduleDTO dto, Long reqUserId);

    Page<Schedule> findByEventId(Long eventId, Pageable pageable);

    Schedule getScheduleDetail(Long id);

    void deleteSchedule(Long id, Long reqUserId);
}
