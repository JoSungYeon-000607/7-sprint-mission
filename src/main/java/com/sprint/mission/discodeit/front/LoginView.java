package com.sprint.mission.discodeit.front;

import com.sprint.mission.discodeit.utils.AppConfig;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import javax.swing.*;
import java.awt.*;

public class LoginView extends JFrame {

    /**
     * LoginView의 생성자입니다.
     * AppConfig를 통해 필요한 서비스와 데이터 관리 기능을 주입받습니다.
     *
     * @param appConfig 애플리케이션의 전체 설정을 관리하는 객체
     */
    public LoginView(AppConfig appConfig) {
        JCFUserService userService = appConfig.getUserService();

        // --- 프레임(창) 기본 설정 ---
        setTitle("DiscodeIt - Login");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // --- 상단 제목 패널 ---
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("Login");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        // --- 중앙 로그인 정보 입력 패널 ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(new JLabel("ID:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        JTextField idField = new JTextField(15);
        centerPanel.add(idField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        centerPanel.add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        JPasswordField passwordField = new JPasswordField(15);
        centerPanel.add(passwordField, gbc);

        add(centerPanel, BorderLayout.CENTER);

        // --- 하단 버튼 패널 ---
        JPanel buttonPanel = new JPanel();
        JButton loginButton = new JButton("로그인");
        JButton registerButton = new JButton("회원가입");
        buttonPanel.add(loginButton);
        buttonPanel.add(registerButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // --- 버튼 이벤트 리스너 추가 ---
        loginButton.addActionListener(e -> {
            String username = idField.getText();
            String password = new String(passwordField.getPassword());
            // TODO: 실제 로그인 로직 구현
            JOptionPane.showMessageDialog(LoginView.this,
                    "ID: " + username + "\nPassword: " + password,
                    "Login Attempt",
                    JOptionPane.INFORMATION_MESSAGE);
        });

        // '회원가입' 버튼 클릭 시 RegisterView를 생성하고 현재 창을 숨김
        registerButton.addActionListener(e -> {
            this.setVisible(false); // 로그인 창 숨기기
            RegisterView registerView = new RegisterView(userService, appConfig, this);
            registerView.setVisible(true);
        });
    }

    // main 메서드는 FrontController로 이동하여 제거합니다.
}