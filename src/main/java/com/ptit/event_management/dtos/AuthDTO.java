package com.ptit.event_management.dtos;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthDTO {
    String email;
    String password;
    String retypePassword;
    String fullName;
    Long roleId;
    String phone;
}