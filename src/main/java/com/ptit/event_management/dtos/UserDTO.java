package com.ptit.event_management.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {
    String fullName;
    String avatar;
    String phone;
}
