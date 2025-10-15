package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.entity.Channel;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class ServerListCell extends ListCell<Channel> {
    private final StackPane graphicPane = new StackPane();
    private final Circle circle = new Circle(25); // 50px diameter
    private final Label label = new Label();

    public ServerListCell() {
        super();
        circle.setFill(Color.web("#313338")); // Default server color
        label.setTextFill(Color.WHITE);
        label.setFont(Font.font("System", FontWeight.BOLD, 16));
        graphicPane.getChildren().addAll(circle, label);
        setAlignment(Pos.CENTER);
        // Remove default cell styling
        setStyle("-fx-background-color: transparent; -fx-padding: 5 0 5 0;");
    }

    @Override
    protected void updateItem(Channel channel, boolean empty) {
        super.updateItem(channel, empty);
        if (empty || channel == null) {
            setGraphic(null);
        } else {
            // Use the first two letters of the channel name for the icon
            label.setText(channel.getChannelName().substring(0, Math.min(channel.getChannelName().length(), 2)).toUpperCase());
            setGraphic(graphicPane);
        }
    }
}