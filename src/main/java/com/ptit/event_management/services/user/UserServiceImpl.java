package com.ptit.event_management.services.user;

import com.ptit.event_management.dtos.UserDTO;
import com.ptit.event_management.models.User;
import com.ptit.event_management.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    private final UserRepository userRepository;
    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found with id = " + id));
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAllByIsVerifiedTrue(pageable);
    }

    @Override
    public User updateUser(Long userId, UserDTO userDTO) {
        User existingUser = findUserById(userId);

        existingUser.setFullName(userDTO.getFullName());
        existingUser.setPhone(userDTO.getPhone());
        existingUser.setAvatar(userDTO.getAvatar());

        return userRepository.save(existingUser);
    }
}
