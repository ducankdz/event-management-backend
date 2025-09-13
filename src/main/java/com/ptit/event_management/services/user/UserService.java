package com.ptit.event_management.services.user;

import com.ptit.event_management.dtos.UserDTO;
import com.ptit.event_management.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    User findUserById(Long id);
    Page<User> getAllUsers(Pageable pageable);
    User updateUser(Long userId, UserDTO userDTO) throws Exception;
}
