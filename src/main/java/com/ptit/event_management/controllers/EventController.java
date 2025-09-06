package com.ptit.event_management.controllers;

import com.ptit.event_management.configurations.JwtTokenUtil;
import com.ptit.event_management.dtos.EventDTO;
import com.ptit.event_management.mappers.EventMapper;
import com.ptit.event_management.models.Event;
import com.ptit.event_management.models.User;
import com.ptit.event_management.responses.AuthResponse;
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
    private final EventMapper eventMapper;
    @PostMapping
    @Operation(
            summary = "Tạo sự kiện mới",
            description = "API này cho phép người dùng tạo mới một sự kiện và lưu vào cơ sở dữ liệu."
    )
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> create(
            @RequestHeader("Authorization") String token,
            @RequestBody @Valid EventDTO dto) {
        try {
            User user = authService.getUserFromToken(token);
//            Long userId = Long.valueOf(JwtTokenUtil.getUserIdFromToken(token));
            Event event = eventService.createEvent(dto, user.getId());
            return new ResponseEntity<>(eventMapper.toEventResponse(event), HttpStatus.CREATED);
        }
        catch (Exception e){
            return new ResponseEntity<>(
                    AuthResponse.builder()
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.OK
            );
        }
    }

//    @PreAuthorize("hasRole('ROLE_USER')")
//    @PutMapping("/{id}")
//    @Operation(
//            summary = "Cập nhật sự kiện",
//            description = "API này cho phép người dùng cập nhật thông tin sự kiện đã tồn tại."
//    )
//    public ResponseEntity<Event> update(
//            @RequestHeader(name = "Authorization") String token,
//            @PathVariable(name = "id") Long id,
//            @RequestBody @Valid EventDto dto) {
//        Long userId = Long.valueOf(JwtTokenUtil.getUserIdFromToken(token));
//        dto.setUpdatedUserId(userId);
//        return new ResponseEntity<>(eventService.update(id, dto), HttpStatus.OK);
//    }
//
//    @PreAuthorize("@eventPermissionEvaluator.hasPermission(#id, 'EVENT',{'event_delete'})")
//    @DeleteMapping("/{id}")
//    @Operation(
//            summary = "Xóa sự kiện",
//            description = "API này cho phép người dùng có quyền xóa một sự kiện theo ID."
//    )
//    public ResponseEntity<?> delete(@PathVariable(name = "id") Long id) {
//        eventService.delete(id);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }
}
