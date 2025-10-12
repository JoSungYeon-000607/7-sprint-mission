package com.sprint.mission.discodeit;

import com.google.gson.reflect.TypeToken;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import com.sprint.mission.discodeit.service.jcf.*;
import com.sprint.mission.discodeit.utils.JsonPersistenceManager;
import com.sprint.mission.discodeit.utils.ParticipationDualKey;

import java.util.HashMap;
import java.util.UUID;

/**
 * 애플리케이션의 전체 구성(Configuration)을 책임지는 클래스입니다.
 * 모든 Repository와 Service 객체를 생성하고, 의존성을 주입하며, 데이터 영속성을 관리합니다.
 */
public class AppConfig {

    // --- 파일 경로 상수 정의 ---
    private static final String USERS_PATH = "users.json";
    private static final String CHANNELS_PATH = "channels.json";
    private static final String PARTICIPATIONS_PATH = "participations.json";
    private static final String CHANNEL_MESSAGES_PATH = "channel_messages.json";
    private static final String DIRECT_MESSAGES_PATH = "direct_messages.json";

    // --- 영속성 관리자 ---
    private final JsonPersistenceManager persistenceManager;

    // --- Repository 인스턴스 ---
    private final JCFUserRepository userRepository;
    private final JCFChannelRepository channelRepository;
    private final JCFParticipationRepository participationRepository;
    private final JCFChannelMessageRepository channelMessageRepository;
    private final JCFDirectMessageRepository directMessageRepository;

    // --- Service 인스턴스 ---
    private final JCFUserService userService;
    private final JCFChannelService channelService;
    private final JCFParticipationService participationService;
    private final JCFChannelMessageService channelMessageService;
    private final JCFDirectMessageService directMessageService;

    public AppConfig() {
        // 1. 영속성 관리자 생성
        this.persistenceManager = new JsonPersistenceManager();

        // 2. 각 Repository 생성
        this.userRepository = new JCFUserRepository();
        this.channelRepository = new JCFChannelRepository();
        this.participationRepository = new JCFParticipationRepository();
        this.channelMessageRepository = new JCFChannelMessageRepository();
        this.directMessageRepository = new JCFDirectMessageRepository();

        // 3. JSON 파일로부터 데이터를 로드하여 각 Repository의 dataMap에 주입
        loadAllData();

        // 4. 각 Service를 생성하며, 의존하는 Repository를 주입
        this.userService = new JCFUserService(userRepository);
        this.channelService = new JCFChannelService(channelRepository);
        this.participationService = new JCFParticipationService(participationRepository, userRepository, channelRepository);
        this.channelMessageService = new JCFChannelMessageService(channelMessageRepository, participationRepository);
        this.directMessageService = new JCFDirectMessageService(directMessageRepository, userRepository);
    }

    /**
     * 모든 데이터를 JSON 파일로부터 로드합니다.
     */
    private void loadAllData() {
        userRepository.loadDataMap(persistenceManager.loadData(USERS_PATH, new TypeToken<HashMap<UUID, User>>() {}.getType()));
        channelRepository.loadDataMap(persistenceManager.loadData(CHANNELS_PATH, new TypeToken<HashMap<UUID, Channel>>() {}.getType()));
        participationRepository.loadDataMap(persistenceManager.loadData(PARTICIPATIONS_PATH, new TypeToken<HashMap<ParticipationDualKey, Participation>>() {}.getType()));
        channelMessageRepository.loadDataMap(persistenceManager.loadData(CHANNEL_MESSAGES_PATH, new TypeToken<HashMap<UUID, ChannelMessage>>() {}.getType()));
        directMessageRepository.loadDataMap(persistenceManager.loadData(DIRECT_MESSAGES_PATH, new TypeToken<HashMap<UUID, DirectMessage>>() {}.getType()));
        System.out.println("모든 데이터를 파일로부터 로드했습니다.");
    }

    /**
     * 현재 메모리에 있는 모든 데이터를 JSON 파일에 저장합니다. (애플리케이션 종료 시 호출)
     */
    public void saveAllData() {
        persistenceManager.saveData(USERS_PATH, userRepository.getDataMap());
        persistenceManager.saveData(CHANNELS_PATH, channelRepository.getDataMap());
        persistenceManager.saveData(PARTICIPATIONS_PATH, participationRepository.getDataMap());
        persistenceManager.saveData(CHANNEL_MESSAGES_PATH, channelMessageRepository.getDataMap());
        persistenceManager.saveData(DIRECT_MESSAGES_PATH, directMessageRepository.getDataMap());
        System.out.println("모든 데이터를 파일에 저장했습니다.");
    }

    // --- Service Getters ---
    // 외부(e.g., main 메서드)에서는 Service를 통해 애플리케이션 로직에 접근합니다.
    public JCFUserService getUserService() {
        return userService;
    }

    public JCFChannelService getChannelService() {
        return channelService;
    }

    public JCFParticipationService getParticipationService() {
        return participationService;
    }

    public JCFChannelMessageService getChannelMessageService() {
        return channelMessageService;
    }

    public JCFDirectMessageService getDirectMessageService() {
        return directMessageService;
    }
}