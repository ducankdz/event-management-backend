package com.ptit.event_management.repositories;

import com.ptit.event_management.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}
