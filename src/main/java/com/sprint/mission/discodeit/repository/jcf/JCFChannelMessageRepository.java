package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ChannelMessage;
import com.sprint.mission.discodeit.repository.ChannelMessageRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFChannelMessageRepository extends JCFBaseRepository<ChannelMessage, UUID> implements ChannelMessageRepository {

    @Override
    public List<ChannelMessage> findByChannelId(UUID channelId) {
        return dataMap.values().stream()
                .filter(cm -> !cm.isDeleted() && cm.getChannelId().equals(channelId))
                .sorted(Comparator.comparing(ChannelMessage::getCreatedAt)) // 생성 시간(오름차순)으로 정렬
                .collect(Collectors.toList());
    }
}