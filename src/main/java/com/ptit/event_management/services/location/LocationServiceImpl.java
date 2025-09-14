package com.ptit.event_management.services.location;

import com.ptit.event_management.dtos.LocationDTO;
import com.ptit.event_management.models.Location;
import com.ptit.event_management.repositories.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService{
    private final LocationRepository locationRepository;
    @Override
    public Page<Location> search(String keyword, Pageable pageable) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return locationRepository.findAll(pageable);
        }
        return locationRepository.findByAvailableTrueAndNameContainingIgnoreCaseOrAvailableTrueAndAddressContainingIgnoreCase(keyword, keyword, pageable);
    }

    @Override
    public Location create(LocationDTO dto) {
        Location location = Location.builder()
                .name(dto.getName())
                .address(dto.getAddress())
                .latitude(dto.getLatitude())
                .longitude(dto.getLongitude())
                .pricePerHour(dto.getPricePerHour())
                .capacity(dto.getCapacity())
                .image(dto.getImage())
                .available(true)
                .build();
        return locationRepository.save(location);
    }

    @Override
    public Location getById(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Location not found with id: " + id));
    }

    @Override
    public Location update(Long id, LocationDTO dto) {
        Location location = getById(id); // lấy ra để update
        location.setName(dto.getName());
        location.setAddress(dto.getAddress());
        location.setLatitude(dto.getLatitude());
        location.setLongitude(dto.getLongitude());
        location.setPricePerHour(dto.getPricePerHour());
        location.setCapacity(dto.getCapacity());
        location.setImage(dto.getImage());
        // available có thể update tùy theo DTO
        if (dto.getAvailable() != null) {
            location.setAvailable(dto.getAvailable());
        }
        return locationRepository.save(location);
    }

    @Override
    public void delete(Long id) {
        Location location = getById(id);
        location.setAvailable(false); // soft delete
        locationRepository.save(location);
    }
}
