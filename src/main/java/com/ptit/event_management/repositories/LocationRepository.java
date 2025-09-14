package com.ptit.event_management.repositories;

import com.ptit.event_management.models.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
    Page<Location> findByAvailableTrueAndNameContainingIgnoreCaseOrAvailableTrueAndAddressContainingIgnoreCase(
            String name, String address, Pageable pageable);

}
