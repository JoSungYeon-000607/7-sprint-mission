package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.auth.service.AuthService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.utils.AppConfig;

public class TestMain {
    public static void main(String[] args) {
        AppConfig appConfig = new AppConfig();
        Runtime.getRuntime().addShutdownHook(new Thread(appConfig::saveAllData));

        JCFUserService userService = appConfig.getUserService();
        AuthService authService = appConfig.getAuthService();

        // --- 멀티 스레드 환경 시뮬레이션 ---
        // 1. 테스트용 유저 생성
        if (!userService.existsByUsernameNonDel("userA")) {
            userService.createUser("userA", "pwA", "userA@a.com", "사용자A", null);
        }
        if (!userService.existsByUsernameNonDel("userB")) {
            userService.createUser("userB", "pwB", "userB@a.com", "사용자B", null);
        }

        // 2. 사용자A의 작업을 정의 (Runnable)
        Runnable userATask = () -> {
            authService.login("userA", "pwA");
            try {
                // 사용자A가 로그인된 상태로 어떤 작업을 수행함
                System.out.println("현재 A 스레드의 유저: " + authService.getCurrentUser().nickname());
                Thread.sleep(500); // 0.5초간 작업
                System.out.println("0.5초 후 A 스레드의 유저: " + authService.getCurrentUser().nickname());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            authService.logout();
        };

        // 3. 사용자B의 작업을 정의 (Runnable)
        Runnable userBTask = () -> {
            authService.login("userB", "pwB");
            // 사용자B가 로그인된 상태로 어떤 작업을 수행함
            System.out.println("현재 B 스레드의 유저: " + authService.getCurrentUser().nickname());
            authService.logout();
        };

        // 4. 두 개의 스레드를 생성하고 거의 동시에 시작
        Thread threadA = new Thread(userATask, "Thread-A");
        Thread threadB = new Thread(userBTask, "Thread-B");

        threadA.start();
        try {
            Thread.sleep(100); // B가 약간 늦게 시작하도록 시뮬레이션
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        threadB.start();
    }
}
