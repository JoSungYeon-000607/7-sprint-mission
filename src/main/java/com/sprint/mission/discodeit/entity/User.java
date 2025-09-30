package com.sprint.mission.discodeit.entity;

import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * 사용자 정보를 담는 핵심 도메인 엔티티입니다.
 * 이 클래스는 사용자의 데이터뿐만 아니라, 자신의 상태를 직접 관리하는 비즈니스 로직을 포함합니다.
 * (풍부한 도메인 모델, Rich Domain Model)
 */
public class User extends BaseEntity implements Identifiable<UUID> {

    // --- Fields ---
    private String username;        // 사용자가 로그인 시 사용하는 ID (Natural Key)
    private String password;
    private String email;
    private String nickname;
    private String phoneNum;
    private State state;            // 사용자의 현재 상태 (ONLINE, OFFLINE 등)
    private Long lastOnlineAt;      // 마지막으로 온라인이었던 시간

    /**
     * 외부에서 `new User()`를 통해 불완전한 객체를 생성하는 것을 막기 위해 생성자를 protected로 선언합니다.
     * 객체 생성은 반드시 `create()` 정적 팩토리 메서드를 통해서만 이루어져야 합니다.
     */
    protected User() {
        // BaseEntity의 생성자를 호출하여 createdAt과 updatedAt을 초기화합니다.
        super();
    }

    /**
     * User 객체를 안전하게 생성하는 정적 팩토리 메서드입니다.
     * 필요한 모든 정보를 받아 유효성 검사를 거친 후, 완전한 상태의 객체를 생성하여 반환합니다.
     *
     * @param username    사용자 이름 (필수)
     * @param rawPassword 암호화되지 않은 비밀번호 (필수)
     * @param email       이메일 (필수)
     * @param nickname    닉네임 (선택)
     * @param phoneNum    전화번호 (선택)
     * @return 완전히 생성된 User 객체
     */
    public static User create(String username, String rawPassword, String email, String nickname, String phoneNum) {
        // Guard Clauses: 객체 생성 전 필수 값들을 검증하여 유효하지 않은 객체가 생성되는 것을 원천 차단합니다.
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("사용자 이름은 필수입니다.");
        }
        if (rawPassword == null || rawPassword.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }

        // 1. protected 생성자를 통해 기본 객체를 생성합니다.
        User user = new User();

        // 2. 전달받은 인자로 필드 값을 설정합니다.
        user.username = username;
        user.password = passwordEncrypt(rawPassword); // 내부 static 헬퍼 메서드를 통해 암호화
        user.email = email;
        user.nickname = nickname;
        user.phoneNum = phoneNum;
        user.state = State.ONLINE; // 사용자는 생성 시 기본적으로 온라인 상태입니다.
        user.lastOnlineAt = user.getCreatedAt(); // 최초 접속 시간은 생성 시간과 동일하게 설정합니다.

        return user;
    }

    /**
     * 사용자 프로필 정보를 수정합니다.
     * 변경된 필드가 있을 경우에만 updatedAt 타임스탬프를 갱신합니다.
     *
     * @param nickname 새 닉네임. null일 경우 변경하지 않음.
     * @param email    새 이메일. null일 경우 변경하지 않음.
     * @param phoneNum 새 전화번호. null일 경우 변경하지 않음.
     */
    public void updateProfile(String nickname, String email, String phoneNum) {
        boolean isChanged = false; // 실제 변경이 일어났는지 추적하는 플래그

        // 닉네임이 null이 아니고, 기존 값과 다를 때만 변경
        if (nickname != null && !nickname.isBlank() && !nickname.equals(this.nickname)) {
            this.nickname = nickname;
            isChanged = true;
        }

        // 이메일이 null이 아니고, 기존 값과 다를 때만 변경
        if (email != null && !email.isBlank() && !email.equals(this.email)) {
            this.email = email;
            isChanged = true;
        }

        // 전화번호가 null이 아니고, 기존 값과 다를 때만 변경
        if (phoneNum != null && !phoneNum.isBlank() && !phoneNum.equals(this.phoneNum)) {
            this.phoneNum = phoneNum;
            isChanged = true;
        }

        // isChanged 플래그를 통해, 불필요한 updatedAt 갱신을 방지합니다.
        if (isChanged) {
            super.updateTimestamp();
        }
    }

    /**
     * 사용자의 비밀번호를 변경합니다.
     *
     * @param newPassword 새로운 비밀번호
     */
    public void changePassword(String newPassword) {
        // 비밀번호 유효성 검사
        if (newPassword == null || newPassword.length() < 8) {
            throw new IllegalArgumentException("비밀번호는 8자 이상이어야 합니다.");
        }
        this.password = passwordEncrypt(newPassword);
        super.updateTimestamp();
    }

    // --- 상태 변경 비즈니스 메서드 ---

    /**
     * '온라인이 된다'는 비즈니스 이벤트를 처리합니다.
     * 상태와 마지막 접속 시간을 함께 변경하여 데이터의 일관성을 유지합니다.
     */
    public void goOnline() {
        this.state = State.ONLINE;
        this.lastOnlineAt = System.currentTimeMillis();
        super.updateTimestamp();
    }

    /**
     * '오프라인이 된다'는 비즈니스 이벤트를 처리합니다.
     */
    public void goOffline() {
        this.state = State.OFFLINE;
        super.updateTimestamp();
    }

    /**
     * '자리 비움으로 설정한다'는 비즈니스 이벤트를 처리합니다.
     * 오프라인 상태에서는 변경할 수 없다는 비즈니스 규칙을 포함합니다.
     */
    public void setAway() { // Away From Keyboard
        if (this.isOffline()) { // 자신의 상태 확인 메서드를 재사용
            throw new IllegalStateException("오프라인 상태에서는 자리 비움으로 변경할 수 없습니다.");
        }
        this.state = State.AFK;
        super.updateTimestamp();
    }

    /**
     * '방해 금지로 설정한다'는 비즈니스 이벤트를 처리합니다.
     */
    public void setDoNotDisturb() {
        if (this.isOffline()) {
            throw new IllegalStateException("오프라인 상태에서는 방해 금지로 변경할 수 없습니다.");
        }
        this.state = State.DND;
        super.updateTimestamp();
    }

    // --- 상태 확인 편의 메서드 ---

    public boolean isOnline() { return this.state == State.ONLINE; }
    public boolean isOffline() { return this.state == State.OFFLINE; }
    public boolean isAway() { return this.state == State.AFK; }
    public boolean isDoNotDisturb() { return this.state == State.DND; }

    // --- private 헬퍼 메서드 ---

    /**
     * 비밀번호를 암호화하는 내부 헬퍼 메서드입니다.
     * @param password 암호화할 원본 비밀번호
     * @return 암호화된 비밀번호
     */
    private static String passwordEncrypt(String password) {
        // TODO: 실제 프로덕션에서는 bcrypt와 같은 검증된 해시 알고리즘을 사용해야 합니다.
        return "encrypted_" + password;
    }

    // --- Getters ---
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPhoneNum() { return phoneNum; }
    public String getNickname() { return nickname; }
    public Long getLastOnlineAt() { return lastOnlineAt; }
    public String getPassword() { return password; }
    public State getState() { return state; }

    // ---toString---
    @Override
    public String toString() {
        return "User{" +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phoneNum='" + phoneNum + '\'' +
                ", state=" + state +
                ", lastOnlineAt=" + lastOnlineAt +
                '}';
    }
}