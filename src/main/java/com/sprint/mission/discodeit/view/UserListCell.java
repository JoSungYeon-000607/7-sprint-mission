package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.entity.User;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class UserListCell extends ListCell<User> {
    private final HBox hbox = new HBox(10);
    private final Circle statusCircle = new Circle(5);
    private final Label nicknameLabel = new Label();

    public UserListCell() {
        super();
        hbox.setPadding(new Insets(5));
        nicknameLabel.setTextFill(Color.WHITE);
        nicknameLabel.setFont(Font.font("System", FontWeight.BOLD, 14));
        hbox.getChildren().addAll(statusCircle, nicknameLabel);
    }

    @Override
    protected void updateItem(User user, boolean empty) {
        super.updateItem(user, empty);
        if (empty || user == null) {
            setText(null);
            setGraphic(null);
        } else {
            nicknameLabel.setText(user.getNickname());
            // 사용자의 상태에 따라 원의 색상 변경
            if (user.isOnline()) {
                statusCircle.setFill(Color.web("#23A559")); // Discord Online Green
            } else {
                statusCircle.setFill(Color.web("#80848E")); // Discord Offline Gray
            }
            setGraphic(hbox);
        }
    }

    public Node getGraphicNode(User user) {
        updateItem(user, false);
        return hbox;
    }
}