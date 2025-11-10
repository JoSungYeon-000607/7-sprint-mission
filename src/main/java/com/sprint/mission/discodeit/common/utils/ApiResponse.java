package com.sprint.mission.discodeit.common.utils;

public record ApiResponse<T>(
        String code,
        String message,
        T data
) {

}