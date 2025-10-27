package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.message.channel.ChannelMessageService;
import com.sprint.mission.discodeit.participation.ParticipationDualKey;
import com.sprint.mission.discodeit.participation.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParticipationManagementService {
    private final ParticipationService participationService;
    private final ChannelMessageService channelMessageService;

    public int countNotReadChannelMessage(UUID channelId, UUID authorId) {

    }
}
