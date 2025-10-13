package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.auth.AuthUser;
import com.sprint.mission.discodeit.auth.service.AuthServiceInterFace;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;
import java.util.Optional;

/**
 * 사용자의 로그인/로그아웃 등 인증 관련 로직을 처리하는 서비스입니다.
 */
public class AuthService implements AuthServiceInterFace {

    private final UserService userService;
    // 각 스레드별로 독립적인 AuthUser 저장 공간을 만듭니다.
    // static final로 선언하여 모든 곳에서 동일한 ThreadLocal 인스턴스를 사용하도록 합니다.
    private static final ThreadLocal<AuthUser> currentSession = new ThreadLocal<>();

    public AuthService(UserService userService) {
        this.userService = userService;
    }

    /**
     * 사용자를 로그인 처리하고, 현재 스레드에 세션을 생성합니다.
     */
    @Override
    public Optional<AuthUser> login(String username, String password) {
        try {
            User user = userService.findByUsernameNonDel(username);

            if (user.getPassword().equals("encrypted_" + password)) {
                AuthUser authUser = AuthUser.from(user);
                userService.goOnline(user.getId());
                // 현재 스레드의 저장 공간에 로그인 정보를 저장합니다.
                currentSession.set(authUser);
                System.out.println("[" + Thread.currentThread().getName() + "] 로그인 성공! 환영합니다, " + authUser.nickname() + "님.");
                return Optional.of(authUser);
            } else {
                System.out.println("[" + Thread.currentThread().getName() + "] 비밀번호가 일치하지 않습니다.");
                return Optional.empty();
            }
        } catch (Exception e) {
            System.out.println("[" + Thread.currentThread().getName() + "] 존재하지 않는 사용자입니다.");
            return Optional.empty();
        }
    }

    /**
     * 현재 스레드의 세션을 제거하여 로그아웃 처리합니다.
     */
    @Override
    public void logout() {
        AuthUser user = currentSession.get();
        if (user != null) {
            System.out.println("[" + Thread.currentThread().getName() + "] " + user.nickname() + "님이 로그아웃하셨습니다.");
            userService.goOffline(user.id());
            // 반드시 remove()를 호출하여 메모리 누수를 방지해야 합니다.
            currentSession.remove();
        }
    }

    /**
     * 현재 스레드에 저장된 사용자 정보를 반환합니다.
     */
    @Override
    public AuthUser getCurrentUser() {
        return currentSession.get();
    }

    /**
     * 현재 스레드가 로그인된 상태인지 확인합니다.
     */
    @Override
    public boolean isLoggedIn() {
        return currentSession.get() != null;
    }
}