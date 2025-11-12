package com.sprint.mission.discodeit.channel.dto;

import com.sprint.mission.discodeit.channel.Channel;

import java.time.Instant;
import java.util.UUID;

public record ChannelResponseDTO(
    UUID id,
    String channelName,
    String description,
    Instant createDate
) {

  public static ChannelResponseDTO from(Channel channel) {
    return new ChannelResponseDTO(
        channel.getId(),
        channel.getChannelName(),
        channel.getDescription(),
        channel.getCreatedAt()
    );
  }
}
