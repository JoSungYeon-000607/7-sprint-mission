import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

class JCFUserServiceTest {

    private JCFUserService userService;
    private JCFUserRepository userRepository;

    @BeforeEach
    void setUp() {
        // 각 테스트는 독립적으로 실행되어야 하므로, 매번 새로운 Repository와 Service 객체를 생성합니다.
        userRepository = new JCFUserRepository();
        userService = new JCFUserService(userRepository);
    }

    @Test
    @DisplayName("새로운 사용자를 성공적으로 생성한다")
    void createUser_should_saveNewUser() {
        // given (준비)
        String username = "testuser";
        String password = "password123";
        String email = "test@example.com";

        // when (실행)
        User createdUser = userService.createUser(username, password, email, null, null);

        // then (검증)
        assertThat(createdUser.getId()).isNotNull();
        assertThat(createdUser.getUsername()).isEqualTo(username);
        assertThat(userRepository.count()).isEqualTo(1); // Repository에 실제로 저장되었는지 확인
    }

    @Test
    @DisplayName("중복된 사용자 이름으로 생성 시 IllegalStateException 예외가 발생한다")
    void createUser_should_throwException_when_usernameExists() {
        // given
        // 먼저 사용자를 하나 생성해둔다.
        userService.createUser("existingUser", "password123", "exist@example.com", null, null);

        // when & then
        // 예외가 발생하는 상황을 검증
        assertThatThrownBy(() -> {
            userService.createUser("existingUser", "new_password", "new@example.com", null, null);
        })
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("이미 존재하는 사용자 이름입니다");
    }

    @Test
    @DisplayName("사용자 프로필을 성공적으로 수정한다")
    void updateProfile_should_changeUserDetails() throws InterruptedException {
        // given
        User user = userService.createUser("userToUpdate", "password", "update@example.com", "OldNick", "123");
        UUID userId = user.getId();
        long initialUpdatedAt = user.getUpdatedAt();
        // when
        User updatedUser = userService.updateProfile(userId, "NewNick", "new.email@example.com", "456");

        // then
        assertThat(updatedUser.getNickname()).isEqualTo("NewNick");
        assertThat(updatedUser.getEmail()).isEqualTo("new.email@example.com");
        assertThat(updatedUser.getPhoneNum()).isEqualTo("456");
        assertThat(updatedUser.getUpdatedAt()).isGreaterThan(initialUpdatedAt); // 수정 시각이 변경되었는지 확인
    }

    @Test
    @DisplayName("사용자 이름으로 사용자를 찾을 수 있다")
    void findByUsername_should_returnUser() {
        // given
        String username = "findMe";
        userService.createUser(username, "password", "find@me.com", null, null);

        // when
        User foundUser = userService.findByUsername(username);

        // then
        assertThat(foundUser).isNotNull();
        assertThat(foundUser.getUsername()).isEqualTo(username);
    }

    @Test
    @DisplayName("존재하지 않는 사용자 이름으로 조회 시 NoSuchElementException 예외가 발생한다")
    void findByUsername_should_throwException_when_userNotFound() {
        // when & then
        assertThatThrownBy(() -> {
            userService.findByUsername("nonExistingUser");
        })
                .isInstanceOf(NoSuchElementException.class);
    }
    @Test
    @DisplayName("물리적 삭제(deleteById) 시 사용자는 저장소에서 완전히 제거된다")
    void deleteById_should_removeUserPermanently() {
        // given
        User user = userService.createUser("deleteUser", "password", "delete@me.com", null, null);
        UUID userId = user.getId();

        // when
        userService.deleteById(userId);

        // then
        assertThat(userRepository.count()).isEqualTo(0);
        assertThat(userRepository.existsById(userId)).isFalse();
    }
}