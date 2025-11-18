package com.sprint.mission.discodeit.user.dto;

import com.sprint.mission.discodeit.content.binary.BinaryContent;
import com.sprint.mission.discodeit.user.User;
import java.time.Instant;
import java.util.UUID;

public record LoginTester(
    UUID id,
    Instant createAt,
    Instant updatedAt,
    String username,
    String email,
    String password,
    UUID profileId
) {

  public static LoginTester from(User user, UUID profileId) {
    return new LoginTester(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getPassword(),
        profileId
    );
  }
}
