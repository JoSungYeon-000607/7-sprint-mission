package com.sprint.mission.discodeit.user;

import com.sprint.mission.discodeit.common.service.BaseService;
import com.sprint.mission.discodeit.user.dto.UserResponseDTO;

import java.util.UUID;

/**
 * 사용자(User) 도메인의 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 * 공통 CRUD 기능은 BaseService로부터 상속받습니다.
 */
public interface UserService extends BaseService<User, UUID> {

    /**
     * 새로운 사용자를 생성하고 저장소에 저장합니다.
     *
     * @param username   사용자 이름 (필수, 고유해야 함)
     * @param password   비밀번호 (필수)
     * @param email      이메일 (필수)
     * @param nickname   닉네임 (선택)
     * @param phoneNum   전화번호 (선택)
     * @return 생성된 User 객체
     */
    User createUser(String username, String password, String email, String nickname, String phoneNum);

    /**
     * 특정 사용자의 프로필 정보(닉네임, 이메일, 전화번호)를 수정합니다.
     *
     * @param userId     수정할 사용자의 고유 ID
     * @param nickname   새로운 닉네임
     * @param email      새로운 이메일
     * @param phoneNum   새로운 전화번호
     * @return 수정이 완료된 User 객체
     */
    UserResponseDTO updateProfile(UUID userId, String nickname, String email, String phoneNum);


    /**
     * 사용자의 비밀번호를 변경합니다.
     *
     * @param userId      비밀번호를 변경할 사용자의 고유 ID
     * @param newPassword 새로운 비밀번호
     */
    void changePassword(UUID userId, String newPassword);

    /**
     * 사용자 이름(username)으로 특정 사용자를 조회합니다.
     *
     * @param username 조회할 사용자의 이름
     * @return 조회된 User 객체
     * @throws java.util.NoSuchElementException 해당 이름의 사용자가 없을 경우
     */
    UserResponseDTO findByUsername(String username);

    /**
     * 특정 사용자 이름이 이미 존재하는지 확인합니다.
     *
     * @param username 확인할 사용자 이름
     * @return 존재하면 true, 존재하지 않으면 false
     */
    boolean existsByUsername(String username);


    UserResponseDTO findByUsernameNonDel(String username);

    boolean existsByUsernameNonDel(String username);
}