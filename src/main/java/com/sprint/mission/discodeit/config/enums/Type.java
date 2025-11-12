package com.sprint.mission.discodeit.config.enums;

public enum Type {
  PUBLIC("공개"),
  PRIVATE("비공개");
  // private final로 선언하여 불변성 보장
  private final String desc;

  // 생성자: enum 상수가 생성될 때 문자열을 받아 필드에 할당
  Type(String desc) {
    this.desc = desc;
  }

  // 외부에서 설명을 가져갈 수 있는 Getter 메서드
  public String getDescription() {
    return desc;
  }
}
