package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.utils.AppConfig;
import java.util.List;
import java.util.Scanner;

public class ChannelView {
    private final JCFChannelService channelService;
    private final Scanner sc;
    private final SharedView sharedView;

    public ChannelView(AppConfig appConfig, Scanner scanner, SharedView sharedView) {
        this.channelService = appConfig.getChannelService();
        this.sc = scanner;
        this.sharedView = sharedView;
    }

    public void showChannelMenu() {
        while (true) {
            System.out.println("\n--- 채널 관리 ---");
            System.out.println("1. 채널 생성");
            System.out.println("2. 채널 설정 변경");
            System.out.println("3. 채널 논리적 삭제");
            System.out.println("4. 채널 물리적 삭제");
            System.out.println("5. 모든 채널 조회");
            System.out.println("6. 활성 채널 조회");
            System.out.println("0. 이전 메뉴로");
            System.out.print(">> 채널 관리 메뉴: ");

            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1:
                        createChannel();
                        break;
                    case 2:
                        changeChannelSettings();
                        break;
                    case 3:
                        softDeleteChannel();
                        break;
                    case 4:
                        deleteChannel();
                        break;
                    case 5:
                        channelService.findAll().forEach(System.out::println);
                        break;
                    case 6:
                        channelService.findAllNonDel().forEach(System.out::println);
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("잘못된 입력입니다.");
                }
            } catch (Exception e) {
                System.out.println("작업 중 오류 발생: " + e.getMessage());
            }
        }
    }

    private void createChannel() {
        System.out.print("채널 이름: ");
        String name = sc.nextLine();
        Channel newChannel = channelService.create(name, null, null, false);
        System.out.println("채널 생성 완료: " + newChannel);
    }

    private void changeChannelSettings() {
        Channel channel = selectChannelFromList(true);
        if (channel == null) return;

        System.out.print("새 채널 이름 (변경 없으면 Enter): ");
        String newName = sc.nextLine();
        channelService.changeSettings(channel.getId(), newName.isBlank() ? null : newName, null, null, null);
        System.out.println("채널 설정 변경 완료.");
    }

    private void softDeleteChannel() {
        Channel channel = selectChannelFromList(true);
        if (channel == null) return;
        channelService.softDeleteById(channel.getId());
        System.out.println("'" + channel.getChannelName() + "' 채널 논리적 삭제 완료.");
    }

    private void deleteChannel() {
        Channel channel = selectChannelFromList(false);
        if (channel == null) return;
        channelService.deleteById(channel.getId());
        System.out.println("'" + channel.getChannelName() + "' 채널 물리적 삭제 완료.");
    }

    // 공통 헬퍼 메서드
    public Channel selectChannelFromList(boolean nonDeletedOnly) {
        return sharedView.selectChannelFromList(nonDeletedOnly);
    }
}