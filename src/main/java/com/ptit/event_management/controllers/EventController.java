package com.ptit.event_management.controllers;

import com.ptit.event_management.configurations.JwtTokenUtil;
import com.ptit.event_management.dtos.EventDTO;
import com.ptit.event_management.mappers.EventMapper;
import com.ptit.event_management.models.Event;
import com.ptit.event_management.models.User;
import com.ptit.event_management.responses.AuthResponse;
import com.ptit.event_management.responses.EventResponse;
import com.ptit.event_management.responses.MessageResponse;
import com.ptit.event_management.responses.UserResponse;
import com.ptit.event_management.services.auth.AuthService;
import com.ptit.event_management.services.event.EventService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {
    private final EventService eventService;
    private final AuthService authService;
    @PostMapping
    @Operation(
            summary = "Tạo sự kiện mới",
            description = "API này cho phép người dùng tạo mới một sự kiện và lưu vào cơ sở dữ liệu."
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> createEvent(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid EventDTO dto) {
        try {
            User user = authService.getUserFromToken(token);
            Event event = eventService.createEvent(dto, user.getId());
            UserResponse userResponse = UserResponse.fromUser(event.getOwner());
            EventResponse eventResponse = EventResponse.fromEvent(event);
            eventResponse.setOwner(userResponse);
            return new ResponseEntity<>(eventResponse, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.OK
            );
        }
    }
    @GetMapping("/{id:\\d+}")
    @Operation(
            summary = "Lấy chi tiết sự kiện",
            description = "API này trả về thông tin chi tiết của một sự kiện theo ID.")
    public ResponseEntity<?> getEventDetail(
            @RequestHeader("Authorization") String token,
            @PathVariable(name = "id") Long id) {
        try {
            User user = authService.getUserFromToken(token);
            Event event = eventService.getEventDetail(id);
            UserResponse userResponse = UserResponse.fromUser(event.getOwner());
            EventResponse eventResponse = EventResponse.fromEvent(event);
            eventResponse.setOwner(userResponse);
            return new ResponseEntity<>(eventResponse, HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.OK
            );
        }
    }
    @PreAuthorize("hasRole('ROLE_USER')")
    @PutMapping("/{id}")
    @Operation(
            summary = "Cập nhật sự kiện",
            description = "API này cho phép người dùng cập nhật thông tin sự kiện đã tồn tại."
    )
    public ResponseEntity<?> updateEvent(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable(name = "id") Long id,
            @RequestBody @Valid EventDTO dto) {
        try {
            User user = authService.getUserFromToken(token);
            Event event = eventService.updateEvent(id, dto, user.getId());
            UserResponse userResponse = UserResponse.fromUser(event.getOwner());
            EventResponse eventResponse = EventResponse.fromEvent(event);
            eventResponse.setOwner(userResponse);
            return new ResponseEntity<>(eventResponse, HttpStatus.OK);
        }
        catch (Exception e){
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.OK
            );
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(
            summary = "Xóa sự kiện",
            description = "API này cho phép người dùng có quyền xóa một sự kiện theo ID."
    )
    public ResponseEntity<?> deleteEvent(
            @RequestHeader(name = "Authorization") String token,
            @PathVariable(name = "id") Long id) {
        try {
            User user = authService.getUserFromToken(token);
            eventService.deleteEvent(id, user.getId());
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message("Delete event successfully")
                            .build(),
                    HttpStatus.OK
            );
        }
        catch (Exception e){
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.OK
            );
        }
    }
}
