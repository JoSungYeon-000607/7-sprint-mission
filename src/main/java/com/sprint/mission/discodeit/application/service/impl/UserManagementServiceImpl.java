package com.sprint.mission.discodeit.application.service.impl;

import com.sprint.mission.discodeit.application.service.UserManagementService;
import com.sprint.mission.discodeit.content.binary.BinaryContentService;
import com.sprint.mission.discodeit.message.channel.ChannelMessageService;
import com.sprint.mission.discodeit.message.direct.DirectMessageService;
import com.sprint.mission.discodeit.participation.ParticipationService;
import com.sprint.mission.discodeit.user.User;
import com.sprint.mission.discodeit.user.UserService;
import com.sprint.mission.discodeit.user.dto.UserRequestDTO;
import com.sprint.mission.discodeit.user.dto.UserResponseDTO;
import com.sprint.mission.discodeit.user.state.UserStatus;
import com.sprint.mission.discodeit.user.state.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserManagementServiceImpl implements UserManagementService {
    private final UserService userService;
    private final ParticipationService participationService;
    private final ChannelMessageService channelMessageService;
    private final DirectMessageService directMessageService;
    private final UserStatusService userStatusService;
    private final BinaryContentService binaryContentService;

    @Override
    public UserResponseDTO createUserWithRelatedData(UserRequestDTO requestDTO) {
        User newUser = userService.createUser(requestDTO);
        newUser.setUserStatus(UserStatus.create(newUser.getId()));

        userService.save(newUser);
        userStatusService.save(newUser.getUserStatus());
        return UserResponseDTO.fromEntity(newUser);
    }

    @Override
    public void deleteUserWithRelatedData(UUID userId) {
        participationService.deleteAllByUserId(userId);

        channelMessageService.deleteAllBySenderId(userId);

        directMessageService.delAllBySenderId(userId);

        userStatusService.deleteByUserId(userId);

        binaryContentService.deleteAllByOwnerId(userId);

        userService.deleteById(userId);
    }
}
