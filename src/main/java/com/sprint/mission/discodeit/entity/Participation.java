package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.Utils.ParticipationDualKey;

import java.util.UUID;

public class Participation extends BaseEntity{
    private ParticipationDualKey participationDualKey;
    private String nickname;
    private Role role;

    protected Participation() {
        super();
    }
    public static Participation create(UUID channelId, UUID userId, String nickname, Role role) {
        if(channelId == null){
            throw new IllegalArgumentException("참여하거나 관리할 채널을 찾을 수 없습니다.");
        }
        if(userId == null){
            throw new IllegalArgumentException("해당 사용자를 찾을 수 없습니다.");
        }
        if(role == null){
            role = Role.USER;
        }
        Participation participation = new Participation();
        participation.participationDualKey = new ParticipationDualKey(channelId, userId);
        participation.nickname = nickname;
        participation.role = role;
        return participation;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
        super.updateTimestamp();
    }
    public void changeRole(Role role) {
        this.role = role;
        super.updateTimestamp();
    }

    public UUID getChannelId() {
        return participationDualKey.channelId();
    }
    public UUID getUserId() {
        return participationDualKey.userId();
    }
    public String getNickname() {
        return nickname;
    }
    public Role getRole() {
        return role;
    }

    @Override
    public String toString() {
        return "Participation{" +
                "channelId=" + participationDualKey.channelId() +
                ", userId=" + participationDualKey.userId() +
                "DualKey=" + participationDualKey +
                ", nickname='" + nickname + '\'' +
                ", role=" + role +
                '}';
    }
}