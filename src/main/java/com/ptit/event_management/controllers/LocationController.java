package com.ptit.event_management.controllers;

import com.ptit.event_management.dtos.LocationDTO;
import com.ptit.event_management.models.Location;
import com.ptit.event_management.responses.MessageResponse;
import com.ptit.event_management.services.auth.AuthService;
import com.ptit.event_management.services.location.LocationService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/location")
@RequiredArgsConstructor
public class LocationController {
    private final LocationService locationService;
    @Operation(summary = "Tìm kiếm địa điểm", description = "Lấy danh sách địa điểm, cho phép tìm kiếm theo từ khóa và phân trang")
    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<Page<Location>> search(
            @RequestParam(defaultValue = "") String keyword,
            Pageable pageable
    ) {
        Page<Location> locations = locationService.search(keyword, pageable);
        return ResponseEntity.ok(locations);
    }

    @Operation(summary = "Tạo mới địa điểm", description = "Thêm một địa điểm mới vào hệ thống")
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Location> create(@RequestBody LocationDTO locationDTO) {
        Location createdLocation = locationService.create(locationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdLocation);
    }

    @Operation(summary = "Xem chi tiết địa điểm", description = "Lấy thông tin chi tiết của một địa điểm theo ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            Location location = locationService.getById(id);
            return ResponseEntity.ok(location);
        }
        catch (Exception e){
            return new ResponseEntity<>(
                    MessageResponse.builder().message(e.getMessage()).build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    @Operation(summary = "Cập nhật địa điểm", description = "Cập nhật thông tin một địa điểm theo ID")
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody LocationDTO locationDTO) {
        try {
            Location location = locationService.update(id, locationDTO);
            return ResponseEntity.ok(location);
        }
        catch (Exception e){
            return new ResponseEntity<>(
                    MessageResponse.builder().message(e.getMessage()).build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    @Operation(summary = "Xóa địa điểm", description = "Xóa mềm một địa điểm theo ID (không xóa vĩnh viễn khỏi cơ sở dữ liệu)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        locationService.delete(id);
        return new ResponseEntity<>(
                MessageResponse.builder().message("Delete location successfully").build(),
                HttpStatus.BAD_REQUEST
        );
    }
}
