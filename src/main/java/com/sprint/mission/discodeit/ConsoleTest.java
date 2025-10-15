package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelMessage;
import com.sprint.mission.discodeit.entity.DirectMessage;
import com.sprint.mission.discodeit.entity.Participation;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannelMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFDirectMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFParticipationService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.utils.AppConfig;
import com.sprint.mission.discodeit.utils.DateUtils;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ConsoleTest {

    // AppConfig로부터 필요한 서비스들을 가져옵니다.
    private static final AppConfig appConfig = new AppConfig();
    private static final JCFUserService userService = appConfig.getUserService();
    private static final JCFChannelService channelService = appConfig.getChannelService();
    private static final JCFParticipationService participationService = appConfig.getParticipationService();
    private static final JCFChannelMessageService channelMessageService = appConfig.getChannelMessageService();
    private static final JCFDirectMessageService directMessageService = appConfig.getDirectMessageService();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        // 프로그램이 종료될 때 데이터를 저장하는 Shutdown Hook을 등록합니다.
        Runtime.getRuntime().addShutdownHook(new Thread(appConfig::saveAllData));

        while (true) {
            try {
                System.out.println("\n==================================================");
                System.out.println(" DiscodeIt 콘솔 테스트 V1.0 ");
                System.out.println("==================================================");
                System.out.println("1. 유저 관리");
                System.out.println("2. 채널 관리");
                System.out.println("3. 참여 관리");
                System.out.println("4. 메시지 관리");
                System.out.println("0. 종료 (데이터 저장)");
                System.out.print(">> 메뉴를 선택하세요: ");

                int choice = sc.nextInt();
                sc.nextLine(); // 버퍼 비우기

                switch (choice) {
                    case 1:
                        handleUserMenu();
                        break;
                    case 2:
                        handleChannelMenu();
                        break;
                    case 3:
                        handleParticipationMenu();
                        break;
                    case 4:
                        handleMessageMenu();
                        break;
                    case 0:
                        System.out.println("프로그램을 종료합니다. 데이터가 저장됩니다.");
                        return; // main 메서드 종료
                    default:
                        System.out.println("잘못된 입력입니다. 다시 시도해주세요.");
                }
            } catch (InputMismatchException e) {
                System.out.println("숫자를 입력해주세요.");
                sc.nextLine(); // 잘못된 입력 버퍼 비우기
            } catch (Exception e) {
                System.out.println("오류가 발생했습니다: " + e.getMessage());
            }
        }
    }

    // -----------------------------유저 관리-----------------------------
    private static void handleUserMenu() {
        while (true) {
            System.out.println("\n--- 유저 관리 ---");
            System.out.println("1. 유저 추가");
            System.out.println("2. 유저 프로필 수정");
            System.out.println("3. 유저 논리적 삭제 (Soft Delete)");
            System.out.println("4. 유저 물리적 삭제 (Hard Delete)");
            System.out.println("5. 모든 유저 조회 (삭제 포함)");
            System.out.println("6. 활성 유저 조회");
            System.out.println("7. 유저 이름으로 조회");
            System.out.println("0. 이전 메뉴로");
            System.out.print(">> 유저 관리 메뉴: ");

            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1:
                        createUser();
                        break;
                    case 2:
                        updateUserProfile();
                        break;
                    case 3:
                        softDeleteUser();
                        break;
                    case 4:
                        deleteUser();
                        break;
                    case 5:
                        userService.findAll().forEach(System.out::println);
                        break;
                    case 6:
                        userService.findAllNonDel().forEach(System.out::println);
                        break;
                    case 7:
                        findUserByUsername();
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

    private static void createUser() {
        System.out.print("사용자 이름(ID): ");
        String username = sc.nextLine();
        System.out.print("비밀번호: ");
        String password = sc.nextLine();
        System.out.print("이메일: ");
        String email = sc.nextLine();
        System.out.print("닉네임: ");
        String nickname = sc.nextLine();
        User newUser = userService.createUser(username, password, email, nickname, null);
        System.out.println("사용자 생성 완료: " + newUser);
    }

    private static void updateUserProfile() {
        System.out.print("수정할 사용자의 이름: ");
        String username = sc.nextLine();
        User user = userService.findByUsernameNonDel(username);
        System.out.print("새 닉네임: ");
        String newNickname = sc.nextLine();
        System.out.print("새 이메일: ");
        String newEmail = sc.nextLine();
        User updatedUser = userService.updateProfile(user.getId(), newNickname, newEmail, null);
        System.out.println("프로필 수정 완료: " + updatedUser);
    }

    private static void softDeleteUser() {
        System.out.print("논리적으로 삭제할 사용자의 이름: ");
        String username = sc.nextLine();
        User user = userService.findByUsernameNonDel(username);
        userService.softDeleteById(user.getId());
        System.out.println("사용자 논리적 삭제 완료.");
    }

    private static void deleteUser() {
        System.out.print("완전히 삭제할 사용자의 이름: ");
        String username = sc.nextLine();
        User userToDelete = userService.findByUsername(username); // isDeleted 포함해서 찾기
        userService.deleteById(userToDelete.getId());
        System.out.println("사용자(" + username + ") 물리적 삭제 완료.");
    }

    private static void findUserByUsername() {
        System.out.print("조회할 사용자 이름: ");
        String username = sc.nextLine();
        User foundUser = userService.findByUsernameNonDel(username);
        System.out.println("조회 결과: " + foundUser);
    }


    // -----------------------------채널 관리 (물리적 삭제 추가)-----------------------------
    private static void handleChannelMenu() {
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

    private static void createChannel() {
        System.out.print("채널 이름: ");
        String name = sc.nextLine();
        Channel newChannel = channelService.create(name, null, null, false);
        System.out.println("채널 생성 완료: " + newChannel);
    }

    private static void changeChannelSettings() {
        System.out.println("--- 설정 변경할 채널 선택 ---");
        Channel channel = selectChannelFromList(true); // 활성 채널만
        if (channel == null) return;

        System.out.print("새 채널 이름 (변경 없으면 Enter): ");
        String newName = sc.nextLine();
        channelService.changeSettings(channel.getId(), newName.isBlank() ? null : newName, null, null, null);
        System.out.println("채널 설정 변경 완료.");
    }

    private static void softDeleteChannel() {
        System.out.println("--- 논리적으로 삭제할 채널 선택 ---");
        Channel channel = selectChannelFromList(true); // 활성 채널만
        if (channel == null) return;

        channelService.softDeleteById(channel.getId());
        System.out.println("'" + channel.getChannelName() + "' 채널 논리적 삭제 완료.");
    }

    private static void deleteChannel() {
        System.out.println("--- 물리적으로 삭제할 채널 선택 ---");
        Channel channel = selectChannelFromList(false); // 모든 채널
        if (channel == null) return;

        channelService.deleteById(channel.getId());
        System.out.println("'" + channel.getChannelName() + "' 채널 물리적 삭제 완료.");
    }

    private static Channel selectChannelFromList(boolean nonDeletedOnly) {
        List<Channel> channels = nonDeletedOnly ? channelService.findAllNonDel() : channelService.findAll();
        if (channels.isEmpty()) {
            System.out.println("선택할 수 있는 채널이 없습니다.");
            return null;
        }

        for (int i = 0; i < channels.size(); i++) {
            Channel ch = channels.get(i);
            System.out.printf("%d. %s (ID: %s, 삭제됨: %s)\n", i + 1, ch.getChannelName(), ch.getId(), ch.isDeleted());
        }
        System.out.println("0. 취소");
        System.out.print(">> 번호를 선택하세요: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice > 0 && choice <= channels.size()) {
            return channels.get(choice - 1);
        }
        return null;
    }


    // -----------------------------참여 관리 (물리적 삭제 추가)-----------------------------
    private static void handleParticipationMenu() {
        while (true) {
            System.out.println("\n--- 참여 관리 ---");
            System.out.println("1. 채널 참여하기");
            System.out.println("2. 채널 나가기 (논리적 삭제)");
            System.out.println("3. 참여 정보 물리적 삭제");
            System.out.println("4. 특정 채널의 참여자 목록 조회");
            System.out.println("5. 특정 유저의 참여 채널 목록 조회");
            System.out.println("0. 이전 메뉴로");
            System.out.print(">> 참여 관리 메뉴: ");

            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1:
                        joinChannel();
                        break;
                    case 2:
                        leaveChannel();
                        break;
                    case 3:
                        deleteParticipation();
                        break;
                    case 4:
                        findParticipantsByChannel();
                        break;
                    case 5:
                        findChannelsByUser();
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

    private static void joinChannel() {
        System.out.println("--- 참여할 사용자 선택 ---");
        User user = selectUserFromList(true);
        if (user == null) return;

        System.out.println("--- 참여할 채널 선택 ---");
        Channel channel = selectChannelFromList(true);
        if (channel == null) return;

        System.out.print("채널에서 사용할 닉네임: ");
        String nickname = sc.nextLine();
        participationService.joinChannel(channel.getId(), user.getId(), nickname);
        System.out.println("'" + user.getUsername() + "'님이 '" + channel.getChannelName() + "' 채널에 참여 완료.");
    }

    private static void leaveChannel() {
        System.out.println("--- 나갈 사용자 선택 ---");
        User user = selectUserFromList(true);
        if (user == null) return;

        Participation participation = selectParticipationFromUser(user);
        if (participation == null) return;

        participationService.leaveChannel(participation.getChannelId(), user.getId());
        System.out.println("채널 나가기 완료.");
    }

    private static void deleteParticipation() {
        System.out.println("--- 참여 정보를 삭제할 사용자 선택 ---");
        User user = selectUserFromList(false); // 모든 사용자
        if (user == null) return;

        Participation participation = selectParticipationFromUser(user);
        if (participation == null) return;

        participationService.deleteById(participation.getId());
        System.out.println("참여 정보 물리적 삭제 완료.");
    }


    private static void findParticipantsByChannel() {
        System.out.println("--- 참여자 목록을 조회할 채널 선택 ---");
        Channel channel = selectChannelFromList(true);
        if (channel == null) return;

        System.out.println("--- '" + channel.getChannelName() + "' 참여자 목록 ---");
        participationService.findParticipationsByChannelId(channel.getId())
                .forEach(p -> {
                    try {
                        User user = userService.findById(p.getUserId());
                        System.out.println("- " + user.getUsername() + " (닉네임: " + p.getNickname() + ", 역할: " + p.getRole() + ")");
                    } catch (Exception e) {
                        System.out.println("- " + p.getUserId() + " (사용자 정보 없음)");
                    }
                });
    }

    private static void findChannelsByUser() {
        System.out.println("--- 조회할 사용자 선택 ---");
        User user = selectUserFromList(true);
        if (user == null) return;

        System.out.println("--- '" + user.getUsername() + "'님이 참여 중인 채널 목록 ---");
        participationService.findParticipationsByUserId(user.getId())
                .forEach(p -> {
                    try {
                        Channel channel = channelService.findById(p.getChannelId());
                        System.out.println("- " + channel.getChannelName() + " (사용 닉네임: " + p.getNickname() + ")");
                    } catch (Exception e) {
                        System.out.println("- " + p.getChannelId() + " (채널 정보 없음)");
                    }
                });
    }

    // -----------------------------메시지 관리 (신규 추가)-----------------------------
    private static void handleMessageMenu() {
        while(true) {
            System.out.println("\n--- 메시지 관리 ---");
            System.out.println("1. 채널 메시지 보내기");
            System.out.println("2. DM(1:1 메시지) 보내기");
            System.out.println("3. 채널 메시지 조회");
            System.out.println("4. DM 대화 조회");
            System.out.println("5. 채널 메시지 논리적 삭제");
            System.out.println("6. DM 물리적 삭제");
            System.out.println("0. 이전 메뉴로");
            System.out.print(">> 메시지 관리 메뉴: ");

            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1:
                        sendChannelMessage();
                        break;
                    case 2:
                        sendDirectMessage();
                        break;
                    case 3:
                        viewChannelMessages();
                        break;
                    case 4:
                        viewDirectMessageConversation();
                        break;
                    case 5:
                        softDeleteChannelMessage();
                        break;
                    case 6:
                        deleteDirectMessage();
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

    private static void sendChannelMessage() {
        System.out.println("--- 메시지를 보낼 사용자 선택 ---");
        User sender = selectUserFromList(true);
        if (sender == null) return;

        System.out.println("--- 메시지를 보낼 채널 선택 ---");
        Participation participation = selectParticipationFromUser(sender);
        if (participation == null) {
            System.out.println("참여 중인 채널이 없어 메시지를 보낼 수 없습니다.");
            return;
        }
        Channel channel = channelService.findById(participation.getChannelId());

        System.out.print("보낼 메시지 내용: ");
        String message = sc.nextLine();
        channelMessageService.sendMessage(channel.getId(), sender.getId(), message);
        System.out.println("메시지 전송 완료.");
    }

    private static void sendDirectMessage() {
        System.out.println("--- 메시지를 보낼 사용자 선택 ---");
        User sender = selectUserFromList(true);
        if (sender == null) return;

        System.out.println("--- 메시지를 받을 사용자 선택 ---");
        User receiver = selectUserFromList(true);
        if (receiver == null) return;

        if(sender.getId().equals(receiver.getId())) {
            System.out.println("자기 자신에게는 DM을 보낼 수 없습니다.");
            return;
        }

        System.out.print("보낼 메시지 내용: ");
        String message = sc.nextLine();
        directMessageService.sendMessage(sender.getId(), receiver.getId(), message);
        System.out.println("DM 전송 완료.");
    }

    private static void viewChannelMessages() {
        System.out.println("--- 메시지를 조회할 채널 선택 ---");
        Channel channel = selectChannelFromList(true);
        if (channel == null) return;

        System.out.println("--- ["+channel.getChannelName()+"] 채널 메시지 목록 ---");
        channelMessageService.getMessagesByChannel(channel.getId())
                .forEach(msg -> {
                    User sender = userService.findById(msg.getSenderId());
                    System.out.printf("[%s] %s: %s\n",
                            DateUtils.formatMillis(msg.getCreatedAt()),
                            sender.getNickname(),
                            msg.getMessage());
                });
    }

    private static void viewDirectMessageConversation() {
        System.out.println("--- 첫 번째 대화 상대 선택 ---");
        User userOne = selectUserFromList(true);
        if (userOne == null) return;

        System.out.println("--- 두 번째 대화 상대 선택 ---");
        User userTwo = selectUserFromList(true);
        if (userTwo == null) return;

        System.out.println("--- ["+userOne.getUsername()+" ↔ "+userTwo.getUsername()+"] 대화 내용 ---");
        directMessageService.getConversation(userOne.getId(), userTwo.getId())
                .forEach(msg -> {
                    User sender = userService.findById(msg.getSenderId());
                    System.out.printf("[%s] %s: %s\n",
                            DateUtils.formatMillis(msg.getCreatedAt()),
                            sender.getNickname(),
                            msg.getMessage());
                });
    }

    private static void softDeleteChannelMessage() {
        System.out.println("--- 메시지를 삭제할 채널 선택 ---");
        Channel channel = selectChannelFromList(true);
        if (channel == null) return;

        List<ChannelMessage> messages = channelMessageService.getMessagesByChannel(channel.getId());
        if(messages.isEmpty()){
            System.out.println("삭제할 메시지가 없습니다.");
            return;
        }
        for(int i=0; i<messages.size(); i++){
            ChannelMessage msg = messages.get(i);
            User sender = userService.findById(msg.getSenderId());
            System.out.printf("%d. [%s] %s: %s\n", i + 1, DateUtils.formatMillis(msg.getCreatedAt()), sender.getNickname(), msg.getMessage());
        }
        System.out.println("0. 취소");
        System.out.print(">> 삭제할 메시지 번호 선택: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice > 0 && choice <= messages.size()) {
            ChannelMessage msgToDelete = messages.get(choice - 1);
            channelMessageService.softDeleteById(msgToDelete.getId());
            System.out.println("채널 메시지 논리적 삭제 완료.");
        }
    }

    private static void deleteDirectMessage() {
        System.out.println("--- DM을 삭제할 첫 번째 사용자 선택 ---");
        User userOne = selectUserFromList(true);
        if (userOne == null) return;

        System.out.println("--- DM을 삭제할 두 번째 사용자 선택 ---");
        User userTwo = selectUserFromList(true);
        if (userTwo == null) return;

        List<DirectMessage> messages = directMessageService.getConversation(userOne.getId(), userTwo.getId());
        if(messages.isEmpty()){
            System.out.println("삭제할 메시지가 없습니다.");
            return;
        }
        for(int i=0; i<messages.size(); i++){
            DirectMessage msg = messages.get(i);
            User sender = userService.findById(msg.getSenderId());
            System.out.printf("%d. [%s] %s: %s\n", i + 1, DateUtils.formatMillis(msg.getCreatedAt()), sender.getNickname(), msg.getMessage());
        }
        System.out.println("0. 취소");
        System.out.print(">> 물리적으로 삭제할 메시지 번호 선택: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice > 0 && choice <= messages.size()) {
            DirectMessage msgToDelete = messages.get(choice - 1);
            directMessageService.deleteById(msgToDelete.getId());
            System.out.println("DM 물리적 삭제 완료.");
        }
    }

    // -----------------------------헬퍼 메서드-----------------------------
    private static User selectUserFromList(boolean nonDeletedOnly) {
        List<User> users = nonDeletedOnly ? userService.findAllNonDel() : userService.findAll();
        if (users.isEmpty()) {
            System.out.println("선택할 수 있는 사용자가 없습니다.");
            return null;
        }

        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            System.out.printf("%d. %s (닉네임: %s, 삭제됨: %s)\n", i + 1, u.getUsername(), u.getNickname(), u.isDeleted());
        }
        System.out.println("0. 취소");
        System.out.print(">> 번호를 선택하세요: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice > 0 && choice <= users.size()) {
            return users.get(choice - 1);
        }
        return null;
    }

    private static Participation selectParticipationFromUser(User user) {
        List<Participation> participations = participationService.findAll().stream()
                .filter(p -> p.getUserId().equals(user.getId()))
                .collect(Collectors.toList());

        if (participations.isEmpty()) {
            System.out.println("해당 유저의 참여 정보가 없습니다.");
            return null;
        }

        System.out.println("--- 채널 참여 정보 선택 ---");
        for (int i = 0; i < participations.size(); i++) {
            Participation p = participations.get(i);
            Channel c = channelService.findById(p.getChannelId());
            System.out.printf("%d. 채널: %s (삭제됨: %s)\n", i + 1, c.getChannelName(), p.isDeleted());
        }
        System.out.println("0. 취소");
        System.out.print(">> 번호를 선택하세요: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice > 0 && choice <= participations.size()) {
            return participations.get(choice - 1);
        }
        return null;
    }
}