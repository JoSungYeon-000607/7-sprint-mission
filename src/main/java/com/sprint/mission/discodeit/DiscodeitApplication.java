package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.auth.AuthUser;
import com.sprint.mission.discodeit.utils.AppConfig;
import com.sprint.mission.discodeit.utils.ConfigurationLoader;
import com.sprint.mission.discodeit.view.MainViewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class DiscodeitApplication extends Application {

    private static Stage primaryStage;
    private static AppConfig appConfig;
    private static ConfigurationLoader configLoader;

    public static void setAppConfig(AppConfig config) {
        appConfig = config;
        configLoader = appConfig.getConfigLoader(); // AppConfig로부터 ConfigLoader를 얻음
    }


    public static AppConfig getAppConfig() {
        return appConfig;
    }

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;
        showLoginView();
    }

    public static void showLoginView() throws IOException {
        FXMLLoader loader = createLoaderFromKey("view.login");
        Parent root = loader.load();
        primaryStage.setTitle("Discodeit - Login");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void showRegisterView() throws IOException {
        FXMLLoader loader = createLoaderFromKey("view.register");
        Parent root = loader.load();
        primaryStage.setTitle("Discodeit - Register");
        primaryStage.setScene(new Scene(root, 600, 500));
        primaryStage.show();
    }

    public static void showMainView(AuthUser authUser) throws IOException {
        FXMLLoader loader = createLoaderFromKey("view.main");
        Parent root = loader.load();

        MainViewController controller = loader.getController();
        controller.initData(authUser);

        primaryStage.setTitle("Discodeit");
        primaryStage.setScene(new Scene(root, 1280, 720));
        primaryStage.show();
    }

    private static FXMLLoader createLoaderFromKey(String fxmlKey) {
        // 1. ConfigLoader로부터 경로 문자열을 가져옵니다.
        String fxmlPath = configLoader.getProperty(fxmlKey);
        if (fxmlPath == null) {
            throw new RuntimeException("설정 키를 찾을 수 없습니다: " + fxmlKey);
        }

        // 2. 경로 문자열을 사용하여 실제 리소스 URL을 찾습니다.
        URL location = DiscodeitApplication.class.getResource(fxmlPath);
        if (location == null) {
            throw new RuntimeException("FXML 파일을 찾을 수 없습니다: " + fxmlPath);
        }

        // 3. FXMLLoader를 생성하여 반환합니다.
        return new FXMLLoader(location);
    }

    @Override
    public void stop() {
        if (appConfig != null) {
            System.out.println("애플리케이션을 종료하며 데이터를 저장합니다...");
            appConfig.saveAllData();
        }
    }
}