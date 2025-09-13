package com.ptit.event_management.controllers;

import com.ptit.event_management.configurations.JwtTokenUtil;
import com.ptit.event_management.dtos.EventDTO;
import com.ptit.event_management.dtos.EventFilterDTO;
import com.ptit.event_management.models.Event;
import com.ptit.event_management.models.EventStatus;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
                    HttpStatus.BAD_REQUEST
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
                    HttpStatus.BAD_REQUEST
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
                    HttpStatus.BAD_REQUEST
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
                    HttpStatus.BAD_REQUEST
            );
        }
    }
    // Lấy tất cả sự kiện (phân trang)
    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Lấy tất cả sự kiện", description = "API trả về danh sách sự kiện có phân trang.")
    public ResponseEntity<?> getAllEvents(
            @RequestHeader("Authorization") String token,
            Pageable pageable) {
        try {
            User user = authService.getUserFromToken(token);
            return ResponseEntity.ok(
                    eventService.getAll(pageable).map(event -> {
                        EventResponse response = EventResponse.fromEvent(event);
                        response.setOwner(UserResponse.fromUser(event.getOwner()));
                        return response;
                    })
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

//    // Lọc sự kiện
//    @PostMapping("/filter")
//    @Operation(summary = "Lọc sự kiện", description = "API cho phép lọc sự kiện theo điều kiện.")
//    public ResponseEntity<?> filterEvents(
//            @RequestHeader("Authorization") String token,
//            @RequestBody EventFilterDTO filter,
//            Pageable pageable) {
//        try {
//            User user = authService.getUserFromToken(token);
//            return ResponseEntity.ok(
//                    eventService.filter(filter, pageable).map(event -> {
//                        EventResponse response = EventResponse.fromEvent(event);
//                        response.setOwner(UserResponse.fromUser(event.getOwner()));
//                        return response;
//                    })
//            );
//        } catch (Exception e) {
//            return ResponseEntity.ok(MessageResponse.builder().message(e.getMessage()).build());
//        }
//    }

    // Đổi trạng thái sự kiện
    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Đổi trạng thái sự kiện", description = "API cho phép thay đổi trạng thái của sự kiện.")
    public ResponseEntity<?> changeStatus(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestParam EventStatus status) {
        try {
            User user = authService.getUserFromToken(token);
            Event event = eventService.changStatus(id, status);
            EventResponse response = EventResponse.fromEvent(event);
            response.setOwner(UserResponse.fromUser(event.getOwner()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    // Mời user vào sự kiện
    @PostMapping("/{id}/invite")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Mời người dùng tham gia sự kiện", description = "API này cho phép chủ sự kiện mời user tham gia.")
    public ResponseEntity<?> inviteUsers(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @RequestBody ArrayList<Long> userIds) {
        try {
            User user = authService.getUserFromToken(token);
            eventService.inviteUserToEvent(id, userIds, user);
            return ResponseEntity.ok(MessageResponse.builder().message("Invited users successfully").build());
        } catch (Exception e) {
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    // Người dùng chấp nhận lời mời
    @PostMapping("/{id}/accept")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Chấp nhận lời mời", description = "API cho phép người dùng chấp nhận lời mời tham gia sự kiện.")
    public ResponseEntity<?> acceptInvitation(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        try {
            User user = authService.getUserFromToken(token);
            eventService.acceptInvitation(id, user);
            return ResponseEntity.ok(MessageResponse.builder().message("Accepted invitation").build());
        } catch (Exception e) {
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    // Người dùng từ chối lời mời
    @PostMapping("/{id}/decline")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Từ chối lời mời", description = "API cho phép người dùng từ chối lời mời tham gia sự kiện.")
    public ResponseEntity<?> declineInvitation(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id) {
        try {
            User user = authService.getUserFromToken(token);
            eventService.declineInvitation(id, user);
            return ResponseEntity.ok(MessageResponse.builder().message("Declined invitation").build());
        } catch (Exception e) {
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    // Lấy danh sách khách của sự kiện
    @GetMapping("/{id}/guests")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Danh sách khách tham gia sự kiện", description = "API này trả về danh sách người dùng đã chấp nhận lời mời tham gia sự kiện.")
    public ResponseEntity<?> getGuests(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            Pageable pageable) {
        try {
            User user = authService.getUserFromToken(token);
            return ResponseEntity.ok(
                    eventService.getGuestsByEventId(id, pageable).map(UserResponse::fromUser)
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    // Gán địa điểm cho sự kiện
    @PutMapping("/{id}/location/{locationId}")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Gán địa điểm cho sự kiện", description = "API này cho phép chủ sự kiện gán địa điểm.")
    public ResponseEntity<?> setLocation(
            @RequestHeader("Authorization") String token,
            @PathVariable Long id,
            @PathVariable Long locationId) {
        try {
            User user = authService.getUserFromToken(token);
            Event event = eventService.setLocation(id, locationId, user.getId());
            EventResponse response = EventResponse.fromEvent(event);
            response.setOwner(UserResponse.fromUser(event.getOwner()));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

    // Lấy sự kiện của 1 user
    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    @Operation(summary = "Lấy sự kiện của người dùng", description = "API trả về các sự kiện mà user sở hữu.")
    public ResponseEntity<?> getEventsByUser(
            @RequestHeader("Authorization") String token,
            Pageable pageable) {
        try {
            User user = authService.getUserFromToken(token);
            return ResponseEntity.ok(
                    eventService.getEventByUser(user.getId(), pageable).map(event -> {
                        EventResponse response = EventResponse.fromEvent(event);
                        response.setOwner(UserResponse.fromUser(event.getOwner()));
                        return response;
                    })
            );
        } catch (Exception e) {
            return new ResponseEntity<>(
                    MessageResponse.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.BAD_REQUEST
            );
        }
    }

}
