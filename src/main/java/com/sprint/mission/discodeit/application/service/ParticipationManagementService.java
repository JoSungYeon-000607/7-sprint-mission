package com.sprint.mission.discodeit.application.service;

import java.util.UUID;

public interface ParticipationManagementService {
    int countNotReadChannelMessage(UUID channelId, UUID authorId);
}
