package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.DirectMessage;
import com.sprint.mission.discodeit.repository.DirectMessageRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class JCFDirectMessageRepository extends JCFBaseRepository<DirectMessage, UUID> implements DirectMessageRepository {

    @Override
    public List<DirectMessage> findByReceiverId(UUID receiverId) {
        return dataMap.values().stream()
                .filter(dm -> !dm.isDeleted() && dm.getReceiverId().equals(receiverId))
                .sorted(Comparator.comparing(DirectMessage::getCreatedAt)) // 생성 시간 순으로 정렬
                .collect(Collectors.toList());
    }

    @Override
    public List<DirectMessage> findBySenderId(UUID senderId) {
        return dataMap.values().stream()
                .filter(dm -> !dm.isDeleted() && dm.getSenderId().equals(senderId))
                .sorted(Comparator.comparing(DirectMessage::getCreatedAt)) // 생성 시간 순으로 정렬
                .collect(Collectors.toList());
    }

    @Override
    public List<DirectMessage> findByParticipants(UUID userOneId, UUID userTwoId) {
        return dataMap.values().stream()
                .filter(dm -> !dm.isDeleted() &&
                        ((dm.getSenderId().equals(userOneId) && dm.getReceiverId().equals(userTwoId)) ||
                                (dm.getSenderId().equals(userTwoId) && dm.getReceiverId().equals(userOneId)))
                )
                .sorted(Comparator.comparing(DirectMessage::getCreatedAt)) // 생성 시간 순으로 정렬
                .collect(Collectors.toList());
    }
}