package com.sprint.mission.discodeit.auth;

import com.sprint.mission.discodeit.entity.User;
import java.util.UUID;

/**
 * 인증된 사용자의 핵심 정보만 담는 불변(immutable) 객체입니다.
 * 애플리케이션 전반에서 현재 로그인한 사용자를 식별하는 데 사용됩니다.
 *
 * @param id       사용자의 고유 ID
 * @param username 사용자의 로그인 ID
 * @param nickname 화면에 표시될 사용자의 닉네임
 */
public record AuthUser(UUID id, String username, String nickname) {

    /**
     * User 엔티티에서 AuthUser 객체를 안전하게 생성하는 정적 팩토리 메서드입니다.
     *
     * @param user User 엔티티 객체
     * @return 필요한 정보만 추출하여 생성된 AuthUser 객체
     */
    public static AuthUser from(User user) {
        if (user == null) {
            return null;
        }
        return new AuthUser(user.getId(), user.getUsername(), user.getNickname());
    }
}