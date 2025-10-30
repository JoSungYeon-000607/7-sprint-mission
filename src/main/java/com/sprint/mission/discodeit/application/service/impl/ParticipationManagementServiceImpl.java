package com.sprint.mission.discodeit.application.service.impl;

import com.sprint.mission.discodeit.application.service.ParticipationManagementService;
import com.sprint.mission.discodeit.message.channel.ChannelMessageService;
import com.sprint.mission.discodeit.message.channel.dto.ChannelMSGResponseDTO;
import com.sprint.mission.discodeit.participation.Participation;
import com.sprint.mission.discodeit.participation.ParticipationDualKey;
import com.sprint.mission.discodeit.participation.ParticipationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ParticipationManagementServiceImpl implements ParticipationManagementService {
    private final ParticipationService participationService;
    private final ChannelMessageService channelMessageService;

    @Override
    public int countNotReadChannelMessage(UUID channelId, UUID authorId) {
        // 1. 유저의 채널 참여 정보를 가져옵니다. (Participation 엔티티)
        Participation participation;
        try {
            // 참여 정보가 없으면 NoSuchElementException 발생
            participation = participationService.findByIdNonDel(
                    new ParticipationDualKey(channelId, authorId)
            );
        } catch (Exception e) {
            return 0;
        }

        // 2. 해당 채널의 모든 메시지 목록을 가져옵니다.
        List<ChannelMSGResponseDTO> messages = channelMessageService.getMessagesByChannel(channelId);
        if (messages.isEmpty()) {
            return 0; // 채널에 메시지가 없으면 0개
        }

        // 3. 유저가 "마지막으로 채널을 읽은 시간"을 가져옵니다.
        //    (처음 채널에 들어왔다면 null일 수 있습니다)
        Instant lastReadAt = participation.getLastReadAt();

        // 4. [분기 1] 만약 lastReadAt이 null이면, 유저가 채널을 한 번도 안 본 것입니다.
        if (lastReadAt == null) {
            // 모든 메시지가 읽지 않은 메시지입니다.
            return messages.size();
        }

        // 5. [분기 2] 유저가 채널을 본 적이 있다면...
        //    '메시지 생성 시간'이 '마지막 읽은 시간'보다 "이후(isAfter)"인 것만 카운트합니다.
        long unreadCount = messages.stream()
                .filter(message -> message.createdAt().isAfter(lastReadAt)) //
                .count();

        return (int) unreadCount;
    }

}
