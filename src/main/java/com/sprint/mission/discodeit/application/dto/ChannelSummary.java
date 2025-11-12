package com.sprint.mission.discodeit.application.dto;

import com.sprint.mission.discodeit.channel.dto.ChannelResponseDTO;

import java.time.Instant;
import java.util.UUID;

public record ChannelSummary(
    UUID channelId,
    String channelName,
    String description,
    int unreadMessageCount,
    Instant lastMessageDate

) {

  public static ChannelSummary from(ChannelResponseDTO channel, int unreadMessageCount) {
    return new ChannelSummary(
        channel.id(),
        channel.channelName(),
        channel.description(),
        unreadMessageCount,
        Instant.now()
    );

  }
}
