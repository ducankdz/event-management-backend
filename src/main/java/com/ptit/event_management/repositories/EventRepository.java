package com.ptit.event_management.repositories;

import com.ptit.event_management.models.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
    Page<Event> findByOwnerId(Long userId, Pageable pageable);
}
