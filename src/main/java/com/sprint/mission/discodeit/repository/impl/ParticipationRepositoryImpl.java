package com.sprint.mission.discodeit.repository.impl;

import com.sprint.mission.discodeit.entity.Participation;
import com.sprint.mission.discodeit.repository.ParticipationRepository;
import com.sprint.mission.discodeit.utils.ParticipationDualKey;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class ParticipationRepositoryImpl extends BaseRepositoryImpl<Participation, ParticipationDualKey> implements ParticipationRepository {

    @Override
    public List<Participation> findAllByChannelId(UUID channelId) {
        return dataMap.values().stream()
                .filter(p -> !p.isDeleted() && p.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Participation> findAllByUserId(UUID userId) {
        return dataMap.values().stream()
                .filter(p -> !p.isDeleted() && p.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
    @Override
    public List<Participation> findAllByChannelIdIsDel(UUID channelId) {
        return dataMap.values().stream()
                .filter(p -> p.isDeleted() && p.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Participation> findAllByUserIdIsDel(UUID userId) {
        return dataMap.values().stream()
                .filter(p -> p.isDeleted() && p.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}