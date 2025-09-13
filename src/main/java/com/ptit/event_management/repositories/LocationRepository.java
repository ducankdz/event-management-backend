package com.ptit.event_management.repositories;

import com.ptit.event_management.models.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
