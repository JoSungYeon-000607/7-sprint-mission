package com.sprint.mission.discodeit.front;

import com.sprint.mission.discodeit.utils.AppConfig;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import javax.swing.*;
import java.awt.*;

/**
 * 사용자 회원가입을 위한 Swing UI 클래스입니다.
 */
public class RegisterView extends JFrame {

    private final JCFUserService userService;
    private final AppConfig appConfig;
    private final JFrame loginView; // 돌아갈 로그인 화면

    /**
     * RegisterView 생성자
     *
     * @param userService 회원가입 로직 처리를 위한 서비스 객체
     * @param appConfig   데이터 저장을 위해 AppConfig 객체
     * @param loginView   '취소' 시 돌아갈 부모 로그인 창
     */
    public RegisterView(JCFUserService userService, AppConfig appConfig, JFrame loginView) {
        this.userService = userService;
        this.appConfig = appConfig;
        this.loginView = loginView;

        // --- 1. 프레임(창) 기본 설정 ---
        setTitle("DiscodeIt - Register");
        setSize(450, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // 현재 창만 닫기
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- 2. 상단 제목 패널 ---
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Create an Account");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // --- 3. 중앙 회원가입 정보 입력 패널 ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // 필드 생성
        JTextField usernameField = new JTextField(20);
        JPasswordField passwordField = new JPasswordField(20);
        JTextField emailField = new JTextField(20);
        JTextField nicknameField = new JTextField(20);
        JTextField phoneNumField = new JTextField(20);

        // 컴포넌트 배치
        addComponent(centerPanel, new JLabel("Username (ID):"), gbc, 0, 0);
        addComponent(centerPanel, usernameField, gbc, 1, 0);
        addComponent(centerPanel, new JLabel("Password:"), gbc, 0, 1);
        addComponent(centerPanel, passwordField, gbc, 1, 1);
        addComponent(centerPanel, new JLabel("Email:"), gbc, 0, 2);
        addComponent(centerPanel, emailField, gbc, 1, 2);
        addComponent(centerPanel, new JLabel("Nickname:"), gbc, 0, 3);
        addComponent(centerPanel, nicknameField, gbc, 1, 3);
        addComponent(centerPanel, new JLabel("Phone Number:"), gbc, 0, 4);
        addComponent(centerPanel, phoneNumField, gbc, 1, 4);

        add(centerPanel, BorderLayout.CENTER);

        // --- 4. 하단 버튼 패널 ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); // 버튼을 오른쪽에 정렬
        JButton registerButton = new JButton("가입");
        JButton cancelButton = new JButton("취소");
        buttonPanel.add(registerButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- 5. 버튼 이벤트 리스너 추가 ---
        // '가입' 버튼 클릭 이벤트
        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            String email = emailField.getText();
            String nickname = nicknameField.getText();
            String phoneNum = phoneNumField.getText();

            // 필수 값 검증
            if (username.isBlank() || password.isBlank() || email.isBlank()) {
                JOptionPane.showMessageDialog(this, "Username, Password, Email은 필수 항목입니다.", "입력 오류", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                // 사용자 생성 서비스 호출
                userService.createUser(username, password, email, nickname, phoneNum);

                // *** 데이터 파일에 즉시 저장 ***
                appConfig.saveAllData();

                JOptionPane.showMessageDialog(this, "회원가입이 성공적으로 완료되었습니다!", "성공", JOptionPane.INFORMATION_MESSAGE);

                // 성공 후 로그인 화면으로 돌아가기
                this.dispose(); // 현재 창 닫기
                loginView.setVisible(true); // 로그인 창 다시 보이기

            } catch (IllegalStateException ex) {
                // 사용자 이름 중복과 같은 비즈니스 예외 처리
                JOptionPane.showMessageDialog(this, ex.getMessage(), "가입 실패", JOptionPane.WARNING_MESSAGE);
            } catch (Exception ex) {
                // 기타 예외 처리
                JOptionPane.showMessageDialog(this, "알 수 없는 오류가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        });

        // '취소' 버튼 클릭 이벤트
        cancelButton.addActionListener(e -> {
            this.dispose(); // 현재 창 닫기
            loginView.setVisible(true); // 로그인 창 다시 보이기
        });

        // 창 닫기 버튼(X)을 눌렀을 때도 로그인 화면이 다시 나타나도록 처리
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                loginView.setVisible(true);
            }
        });
    }

    /**
     * GridBagLayout에 컴포넌트를 쉽게 추가하기 위한 헬퍼 메서드
     */
    private void addComponent(JPanel panel, JComponent component, GridBagConstraints gbc, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(component, gbc);
    }
}