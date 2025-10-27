package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.message.channel.ChannelMessageService;
import com.sprint.mission.discodeit.message.direct.DirectMessageService;
import com.sprint.mission.discodeit.participation.ParticipationService;
import com.sprint.mission.discodeit.user.User;
import com.sprint.mission.discodeit.user.UserService;
import com.sprint.mission.discodeit.user.dto.UserResponseDTO;
import com.sprint.mission.discodeit.user.state.UserStatus;
import com.sprint.mission.discodeit.user.state.UserStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserManagementService {
    private final UserService userService;
    private final ParticipationService participationService;
    private final ChannelMessageService channelMessageService;
    private final DirectMessageService directMessageService;
    private final UserStatusService userStatusService;

    public UserResponseDTO createUserWithRelatedData(String username, String rawPassword, String email, String nickname, String phoneNum) {
        User newUser = User.createUser(username, rawPassword, email, nickname, phoneNum);
        newUser.setUserStatus(UserStatus.create(newUser.getId()));

        userService.save(newUser);
        userStatusService.save(newUser.getUserStatus());
        return UserResponseDTO.fromEntity(newUser);
    }

    public void deleteUserWithRelatedData(UUID userId) {
        participationService.deleteAllByUserId(userId);

        channelMessageService.deleteAllBySenderId(userId);

        directMessageService.delAllBySenderId(userId);

        userStatusService.deleteByUserId(userId);

        userService.deleteById(userId);
    }
}
