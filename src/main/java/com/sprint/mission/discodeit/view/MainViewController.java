package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.DiscodeitApplication;
import com.sprint.mission.discodeit.auth.AuthUser;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class MainViewController {

    // FXML UI Components
    @FXML private Label nicknameLabel, usernameLabel, chatHeaderLabel, subHeaderLabel;
    @FXML private ListView<Channel> serverListView;
    @FXML private ListView<Object> subListView; // User(DM) or Channel can be displayed
    @FXML private ListView<User> userListView;
    @FXML private VBox chatArea, memberListPanel;
    @FXML private ScrollPane chatScrollPane;
    @FXML private TextField messageField;

    // Services
    private UserService userService;
    private ChannelService channelService;
    private ParticipationService participationService;
    private ChannelMessageService channelMessageService;
    private DirectMessageService directMessageService;

    // State
    private AuthUser currentUser;
    private Stage stage;
    private ObservableList<Channel> userChannels = FXCollections.observableArrayList();
    private ObservableList<User> allUsers = FXCollections.observableArrayList();
    private Object currentChatContext; // Can be a User (for DM) or a Channel

    public void initData(AuthUser user, Stage stage) {
        // Initialize services
        this.userService = DiscodeitApplication.getAppConfig().getUserService();
        this.channelService = DiscodeitApplication.getAppConfig().getChannelService();
        this.participationService = DiscodeitApplication.getAppConfig().getParticipationService();
        this.channelMessageService = DiscodeitApplication.getAppConfig().getChannelMessageService();
        this.directMessageService = DiscodeitApplication.getAppConfig().getDirectMessageService();

        // Initialize state
        this.currentUser = user;
        this.stage = stage;

        // Setup UI
        nicknameLabel.setText(currentUser.nickname());
        usernameLabel.setText(currentUser.username());

        setupServerList();
        setupSubList();
        setupUserList();

        loadInitialData();
        showDmView(); // Start with DM view selected
    }

    private void setupServerList() {
        serverListView.setItems(userChannels);
        serverListView.setCellFactory(param -> new ServerListCell());
        serverListView.getSelectionModel().selectedItemProperty().addListener((obs, old, channel) -> {
            if (channel != null) {
                showChannelView(channel);
            }
        });
    }

    private void setupSubList() {
        subListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else if (item instanceof User) {
                    // It's a DM, use UserListCell's logic
                    setGraphic(new UserListCell().getGraphicNode((User) item));
                }
            }
        });

        subListView.getSelectionModel().selectedItemProperty().addListener((obs, old, selection) -> {
            if (selection instanceof User) {
                loadDirectMessageConversation((User) selection);
            }
        });
    }

    private void setupUserList() {
        userListView.setCellFactory(param -> new UserListCell());
    }

    private void loadInitialData() {
        // Load all channels the user is part of
        List<Participation> participations = participationService.findParticipationsByUserId(currentUser.id());
        List<Channel> channels = participations.stream()
                .map(p -> channelService.findById(p.getChannelId()))
                .collect(Collectors.toList());
        userChannels.setAll(channels);

        // Load all users for DM list (excluding self)
        allUsers.setAll(userService.findAllNonDel().stream()
                .filter(u -> !u.getId().equals(currentUser.id()))
                .collect(Collectors.toList()));
    }

    @FXML
    private void handleDmButtonClick() {
        serverListView.getSelectionModel().clearSelection();
        showDmView();
    }

    private void showDmView() {
        subHeaderLabel.setText("Direct Messages");
        subListView.getItems().setAll(allUsers);
        chatHeaderLabel.setText("Select a friend to start chatting");
        chatArea.getChildren().clear();
        messageField.setDisable(true);
        memberListPanel.setVisible(false); // Hide member list in DM view
        currentChatContext = null;
    }

    private void showChannelView(Channel channel) {
        subHeaderLabel.setText("CHANNELS");
        // In a real scenario, you'd list text channels here. For now, it's empty.
        subListView.getItems().clear();
        memberListPanel.setVisible(true); // Show member list
        loadChannelContent(channel);
    }

    private void loadChannelContent(Channel channel) {
        currentChatContext = channel;
        chatHeaderLabel.setText("# " + channel.getChannelName());
        messageField.setDisable(false);

        List<Participation> participations = participationService.findParticipationsByChannelId(channel.getId());
        List<User> users = participations.stream()
                .map(p -> userService.findById(p.getUserId()))
                .collect(Collectors.toList());
        userListView.getItems().setAll(users);

        chatArea.getChildren().clear();
        List<ChannelMessage> messages = channelMessageService.getMessagesByChannel(channel.getId());
        messages.forEach(this::addMessageToChat);
        scrollToBottom();
    }

    private void loadDirectMessageConversation(User otherUser) {
        currentChatContext = otherUser;
        chatHeaderLabel.setText("@ " + otherUser.getNickname());
        messageField.setDisable(false);
        memberListPanel.setVisible(false);

        chatArea.getChildren().clear();
        List<DirectMessage> conversation = directMessageService.getConversation(currentUser.id(), otherUser.getId());
        conversation.forEach(this::addMessageToChat);
        scrollToBottom();
    }

    @FXML
    private void handleSendMessage() {
        String messageText = messageField.getText();
        if (messageText.isBlank() || currentChatContext == null) return;

        if (currentChatContext instanceof Channel) {
            Channel channel = (Channel) currentChatContext;
            ChannelMessage sentMessage = channelMessageService.sendMessage(channel.getId(), currentUser.id(), messageText);
            addMessageToChat(sentMessage);
        } else if (currentChatContext instanceof User) {
            User otherUser = (User) currentChatContext;
            DirectMessage sentMessage = directMessageService.sendMessage(currentUser.id(), otherUser.getId(), messageText);
            addMessageToChat(sentMessage);
        }

        messageField.clear();
        scrollToBottom();
    }

    private void addMessageToChat(Object messageObject) {
        String senderNickname;
        String messageText;
        UUID senderId;

        if (messageObject instanceof ChannelMessage) {
            ChannelMessage msg = (ChannelMessage) messageObject;
            senderId = msg.getSenderId();
            messageText = msg.getMessage();
        } else if (messageObject instanceof DirectMessage) {
            DirectMessage msg = (DirectMessage) messageObject;
            senderId = msg.getSenderId();
            messageText = msg.getMessage();
        } else {
            return;
        }

        senderNickname = userService.findById(senderId).getNickname();
        Label msgLabel = new Label(senderNickname + ": " + messageText);
        msgLabel.setStyle("-fx-text-fill: white; -fx-wrap-text: true;");
        chatArea.getChildren().add(msgLabel);
    }

    @FXML
    private void handleCreateChannel() {
        TextInputDialog dialog = new TextInputDialog("new-channel");
        dialog.setTitle("Create New Channel");
        dialog.setHeaderText("Enter a name for your new channel.");
        dialog.setContentText("Channel name:");

        dialog.showAndWait().ifPresent(channelName -> {
            try {
                Channel newChannel = channelService.create(channelName, null, "", false);
                participationService.joinChannel(newChannel.getId(), currentUser.id(), currentUser.nickname());
                loadInitialData(); // Refresh server list
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, e.getMessage()).showAndWait();
            }
        });
    }

    private void scrollToBottom() {
        Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
    }
}