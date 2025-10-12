package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JavaApplication {

    public static void main(String[] args) {
        System.out.println("Discodeit 애플리케이션을 시작합니다.");
        System.out.println("------------------------------------------------");

        // 1. AppConfig를 통해 애플리케이션의 모든 컴포넌트를 생성하고 데이터를 로드합니다.
        AppConfig appConfig = new AppConfig();

        // 2. JVM이 종료될 때 AppConfig의 saveAllData 메서드가 실행되도록 등록합니다. (Shutdown Hook)
        // 이렇게 하면 프로그램이 정상 종료될 때 (e.g., Ctrl+C) 자동으로 데이터가 저장됩니다.
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("애플리케이션을 종료하며 데이터를 저장합니다...");
            appConfig.saveAllData();
        }));

        // 3. AppConfig로부터 필요한 Service를 가져와서 사용합니다.
        JCFUserService userService = appConfig.getUserService();
        // JCFChannelService channelService = appConfig.getChannelService();
        // ... 다른 서비스들도 필요에 따라 가져와서 사용

        // --- 예제 로직 ---
        // 애플리케이션이 실행 중일 때 수행할 로직들을 여기에 작성합니다.
        // 예를 들어, 새로운 사용자를 생성해 봅니다.
        try {
            if (!userService.existsByUsernameNonDel("admin")) {
                User admin = userService.createUser("admin", "admin1234", "admin@discodeit.com", "관리자", null);
                System.out.println("관리자 계정이 생성되었습니다: " + admin.getUsername());
            } else {
                System.out.println("관리자 계정이 이미 존재합니다.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("현재 사용자 수: " + userService.countNonDel());
        System.out.println("------------------------------------------------");
        System.out.println("애플리케이션이 실행 중입니다. 종료하려면 Ctrl+C를 누르세요.");
    }
}