package com.sprint.mission.discodeit.common;

public record Event<T>(EventType type, T data) {
    public enum EventType {
        NEW_CHANNEL_MESSAGE,
        NEW_DIRECT_MESSAGE,
        USER_STATUS_CHANGED,
    }
}