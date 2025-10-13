package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.auth.AuthUser;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class MainViewController {

    @FXML
    private Label nicknameLabel;

    @FXML
    private Label usernameLabel;

    @FXML
    private VBox chatArea;

    /**
     * 로그인 성공 후 AuthUser 객체를 받아 UI를 초기화합니다.
     * 이 메서드는 JavaApplication에서 호출됩니다.
     *
     * @param user 로그인한 사용자의 인증 정보
     */
    public void initData(AuthUser user) {
        if (user != null) {
            // AuthUser 객체의 정보를 UI 컨트롤에 바인딩합니다.
            nicknameLabel.setText(user.nickname());
            usernameLabel.setText(user.username());

            // TODO: 이곳에서 user.id()를 사용하여
            // channelService나 directMessageService를 통해
            // 채널 목록, 친구 목록 등을 불러와 UI에 채워 넣는 로직이 추가됩니다.
        }
    }
}