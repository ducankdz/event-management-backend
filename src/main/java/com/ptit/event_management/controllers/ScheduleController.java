package com.ptit.event_management.controllers;

import com.ptit.event_management.dtos.ScheduleDTO;
import com.ptit.event_management.models.Event;
import com.ptit.event_management.models.Schedule;
import com.ptit.event_management.models.User;
import com.ptit.event_management.responses.EventResponse;
import com.ptit.event_management.responses.MessageResponse;
import com.ptit.event_management.responses.ScheduleResponse;
import com.ptit.event_management.services.auth.AuthService;
import com.ptit.event_management.services.schedule.ScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final AuthService authService;

    @Operation(summary = "Tạo lịch trình", description = "Tạo mới một lịch trình cho sự kiện dựa trên ID sự kiện và thông tin người dùng từ token")
    @PostMapping("")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> addSchedule(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody ScheduleDTO dto) {
        try {
            User user = authService.getUserFromToken(token);
            Schedule schedule = scheduleService.addSchedule(dto, user.getId());
            return new ResponseEntity<>(ScheduleResponse.fromSchedule(schedule), HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    @Operation(summary = "Cập nhật lịch trình", description = "Cập nhật thông tin một lịch trình trong sự kiện")
    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> updateSchedule(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable Long id,
            @RequestBody ScheduleDTO dto) {
        try {
            User user = authService.getUserFromToken(token);
            Schedule schedule = scheduleService.updateSchedule(id, dto, user.getId());
            return ResponseEntity.ok(ScheduleResponse.fromSchedule(schedule));
        } catch (Exception e) {
            return new ResponseEntity<>(
                    MessageResponse.builder().message(e.getMessage()).build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    @Operation(summary = "Danh sách lịch trình theo sự kiện", description = "Lấy danh sách lịch trình theo ID sự kiện (có phân trang)")
    @GetMapping("/event/{eventId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getSchedulesByEvent(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable Long eventId,
            Pageable pageable) {
        try {
            authService.getUserFromToken(token);
            Page<Schedule> schedules = scheduleService.findByEventId(eventId, pageable);
            Page<ScheduleResponse> responsePage = schedules.map(ScheduleResponse::fromSchedule);
            return ResponseEntity.ok(responsePage);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    MessageResponse.builder().message(e.getMessage()).build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    @Operation(summary = "Chi tiết lịch trình", description = "Lấy chi tiết một lịch trình theo ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getScheduleDetail(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable Long id) {
        try {
            authService.getUserFromToken(token);
            Schedule schedule = scheduleService.getScheduleDetail(id);
            return ResponseEntity.ok(ScheduleResponse.fromSchedule(schedule));
        } catch (Exception e) {
            return new ResponseEntity<>(
                    MessageResponse.builder().message(e.getMessage()).build(),
                    HttpStatus.NOT_FOUND
            );
        }
    }
    @Operation(summary = "Xóa lịch trình", description = "Xóa một lịch trình theo ID")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> deleteSchedule(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable Long id) {
        try {
            User user = authService.getUserFromToken(token);
            scheduleService.deleteSchedule(id, user.getId());
            return ResponseEntity.ok(
                    MessageResponse.builder().message("Schedule deleted successfully").build()
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    MessageResponse.builder().message(e.getMessage()).build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }
}
