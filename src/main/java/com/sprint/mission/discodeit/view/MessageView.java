package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.utils.AppConfig;
import java.util.Scanner;

public class MessageView {
    private final ChannelMessageView channelMessageView;
    private final DirectMessageView directMessageView;
    private final SharedView sharedView;
    private final Scanner sc;

    public MessageView(AppConfig appConfig, Scanner scanner,SharedView sharedView) {
        this.sharedView = sharedView;
        this.channelMessageView = new ChannelMessageView(appConfig, scanner, sharedView);
        this.directMessageView = new DirectMessageView(appConfig, scanner, sharedView);
        this.sc = scanner;
    }

    public void showMessageMenu() {
        while (true) {
            System.out.println("\n--- 메시지 관리 ---");
            System.out.println("1. 채널 메시지 관리");
            System.out.println("2. DM(1:1 메시지) 관리");
            System.out.println("0. 이전 메뉴로");
            System.out.print(">> 메시지 관리 메뉴: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    channelMessageView.showChannelMessageMenu();
                    break;
                case 2:
                    directMessageView.showDirectMessageMenu();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }
}