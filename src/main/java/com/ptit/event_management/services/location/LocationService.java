package com.ptit.event_management.services.location;

import com.ptit.event_management.dtos.LocationDTO;
import com.ptit.event_management.models.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LocationService {
    Page<Location> search(String keyword, Pageable pageable);
    Location create(LocationDTO dto);
    Location getById(Long id);
    Location update(Long id, LocationDTO dto);
    void delete(Long id);
//    Booking createBooking(BookingDTO bookingDTO, User reqUser);
//    Booking getBookingById(Long id);
//    Page<Booking> getBookingsByUser(User user, Pageable pageable);
}
