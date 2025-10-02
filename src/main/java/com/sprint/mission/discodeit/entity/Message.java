package com.sprint.mission.discodeit.entity;

import java.util.UUID;

public class Message extends BaseEntity<UUID>{

    protected Message() {
        super(UUID.randomUUID());
    }
}
