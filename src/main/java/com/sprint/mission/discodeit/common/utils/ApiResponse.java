//package com.sprint.mission.discodeit.common.utils;
//
//public record ApiResponse<T>(
//    String code,
//    String message,
//    T data
//) {
//
//  public static <T> ApiResponse<T> success(String code, T data) {
//    return new ApiResponse<>(code, "데이터가 성공적으로 처리되었습니다.", data);
//  }
//
//  public static ApiResponse<?> success(String code) {
//    return new ApiResponse<>(code, "성공적으로 처리되었습니다.", null);
//  }
//
//  public static ApiResponse<?> error(String code, String message) {
//    return new ApiResponse<>(code, message, null);
//  }
//}
