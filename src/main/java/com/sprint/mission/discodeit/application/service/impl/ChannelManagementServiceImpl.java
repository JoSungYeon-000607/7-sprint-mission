package com.sprint.mission.discodeit.application.service.impl;

import com.sprint.mission.discodeit.application.service.ChannelManagementService;
import com.sprint.mission.discodeit.channel.ChannelService;
import com.sprint.mission.discodeit.channel.dto.ChannelRequestDTO;
import com.sprint.mission.discodeit.channel.dto.ChannelResponseDTO;
import com.sprint.mission.discodeit.config.enums.Type;
import com.sprint.mission.discodeit.participation.ParticipationService;
import com.sprint.mission.discodeit.user.UserService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChannelManagementServiceImpl implements ChannelManagementService {

  private final ChannelService channelService;
  private final ParticipationService participationService;
  private final UserService userService;

  @Override
  public ChannelResponseDTO createUserWithRelatedData(ChannelRequestDTO requestDTO, Type type,
      UUID user) {
    ChannelResponseDTO channelResponseDTO = ChannelResponseDTO.from(
        channelService.save(channelService.create(requestDTO, type)));
    

  }
}
