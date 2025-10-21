package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.ChannelMessage;
import com.sprint.mission.discodeit.repository.ChannelMessageRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFChannelMessageRepository extends JCFBaseRepository<ChannelMessage, UUID> implements ChannelMessageRepository {

    @Override
    public List<ChannelMessage> findAllByChannelId(UUID channelId) {
        return dataMap.values().stream()
                .filter(cm -> !cm.isDeleted() && cm.getChannelId().equals(channelId))
                .sorted(Comparator.comparing(ChannelMessage::getCreatedAt)) // 생성 시간(오름차순)으로 정렬
                .toList();
    }

    @Override
    public List<ChannelMessage> findAllBySenderId(UUID senderId) {
        return dataMap.values().stream()
                .filter(cm -> !cm.isDeleted() && cm.getSenderId().equals(senderId))
                .sorted(Comparator.comparing(ChannelMessage::getCreatedAt))
                .toList();
    }

    @Override
    public void deleteAllBySenderId(UUID senderId) {
        findAllBySenderId(senderId).stream()
                .map(ChannelMessage::getId).forEach(this::deleteById);
    }


}