package com.sprint.mission.discodeit.application.service;

import com.sprint.mission.discodeit.user.dto.UserRequestDTO;
import com.sprint.mission.discodeit.user.dto.UserResponseDTO;

import java.util.UUID;

public interface UserManagementService {
    UserResponseDTO createUserWithRelatedData(UserRequestDTO requestDTO);
    void deleteUserWithRelatedData(UUID userId);
}
