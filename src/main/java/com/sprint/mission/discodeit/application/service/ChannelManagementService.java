package com.sprint.mission.discodeit.application.service;

import com.sprint.mission.discodeit.channel.dto.ChannelRequestDTO;
import com.sprint.mission.discodeit.channel.dto.ChannelResponseDTO;
import com.sprint.mission.discodeit.config.enums.Type;
import java.util.UUID;

public interface ChannelManagementService {

  ChannelResponseDTO createUserWithRelatedData(ChannelRequestDTO requestDTO, Type type, UUID user);
}
