package com.sprint.mission.discodeit.entity;

public enum ChannelType {
    ONLINE("온라인"),
    AFK("자리비움"),
    DND("방해금지"),
    OFFLINE("오프라인");

    // 3. 필드 선언 (private final로 캡슐화)
    private final String desc;

    // 4. 생성자 선언 (상수가 생성될 때 호출됨)
    ChannelType(String desc) {
        this.desc = desc;
    }
    public String getDescription() {
        return desc;
    }
}
