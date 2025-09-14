package com.ptit.event_management.repositories;

import com.ptit.event_management.models.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Page<Schedule> findByEventId(Long eventId, Pageable pageable);
}
