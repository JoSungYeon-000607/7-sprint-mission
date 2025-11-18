package com.sprint.mission.discodeit.channel;

import com.sprint.mission.discodeit.common.entity.BaseEntity;
import com.sprint.mission.discodeit.config.enums.Type;
import lombok.Getter;

import java.util.UUID;

/**
 * 채널의 정보를 담는 핵심 도메인 엔티티입니다. 채널의 생성과 상태 변경에 대한 비즈니스 로직을 포함합니다.
 */
@Getter
public class Channel extends BaseEntity<UUID> {

  /**
   * 채널의 이름 (사용자에게 표시됨, 고유해야 함)
   */
  private String channelName;
  /**
   * 채널의 주제 또는 설명
   */
  private String description;

  private Type type;

  /**
   * 외부에서의 직접적인 생성을 막고, static create 메서드를 통하도록 강제합니다.
   */
  private Channel() {
    super(UUID.randomUUID());
  }


  public static Channel create(String channelName, String description, Type type) {
    if (channelName == null || channelName.isBlank()) {
      throw new IllegalArgumentException("채널 이름은 필수입니다.");
    }
    Channel channel = new Channel();
    channel.channelName = channelName;
    channel.description = description;
    channel.type = type == null ? Type.PUBLIC : type;
    return channel;
  }

  public void changeSettings(String channelName, String description) {
    boolean isChanged = false;

    if (channelName != null && !channelName.isBlank()) {
      this.channelName = channelName;
      isChanged = true;
    }

    if (description != null) {
      this.description = description;
      isChanged = true;
    }

    // 실제 필드 값이 하나라도 변경되었을 때만 수정 시각을 갱신합니다.
    if (isChanged) {
      super.updateTimestamp();
    }
  }

  @Override
  public String toString() {
    return "Channel{" +
        "id=" + getId() +
        ", channelName='" + channelName + '\'' +
        ", description='" + description + '\'' +
        '}';
  }
}