package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.utils.AppConfig;
import java.util.List;
import java.util.Scanner;

public class UserView {

    private final JCFUserService userService;
    private final Scanner sc;
    private final SharedView sharedView;

    public UserView(AppConfig appConfig, Scanner scanner, SharedView sharedView) {
        this.userService = appConfig.getUserService();
        this.sc = scanner;
        this.sharedView = sharedView;
    }

    public void showUserMenu() {
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

    private void createUser() {
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

    private void updateUserProfile() {
        User user = selectUserFromList(true);
        if (user == null) return;
        System.out.print("새 닉네임: ");
        String newNickname = sc.nextLine();
        System.out.print("새 이메일: ");
        String newEmail = sc.nextLine();
        User updatedUser = userService.updateProfile(user.getId(), newNickname, newEmail, null);
        System.out.println("프로필 수정 완료: " + updatedUser);
    }

    private void softDeleteUser() {
        User user = selectUserFromList(true);
        if (user == null) return;
        userService.softDeleteById(user.getId());
        System.out.println("사용자 논리적 삭제 완료.");
    }

    private void deleteUser() {
        User userToDelete = selectUserFromList(false);
        if (userToDelete == null) return;
        userService.deleteById(userToDelete.getId());
        System.out.println("사용자(" + userToDelete.getUsername() + ") 물리적 삭제 완료.");
    }

    private void findUserByUsername() {
        System.out.print("조회할 사용자 이름: ");
        String username = sc.nextLine();
        User foundUser = userService.findByUsernameNonDel(username);
        System.out.println("조회 결과: " + foundUser);
    }

    // 공통 헬퍼 메서드
    public User selectUserFromList(boolean nonDeletedOnly) {
       return sharedView.selectUserFromList(nonDeletedOnly);
    }
}