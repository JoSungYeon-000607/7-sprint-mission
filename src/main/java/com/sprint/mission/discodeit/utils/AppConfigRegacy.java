package com.sprint.mission.discodeit.utils;

import com.sprint.mission.discodeit.data.DataKey;
import com.sprint.mission.discodeit.data.DataPersistenceManager;
import com.sprint.mission.discodeit.repository.impl.*;
import com.sprint.mission.discodeit.service.jcf.*;

public class AppConfigRegacy {

    // 모든 설정과 컴포넌트를 소유하고 관리합니다.
//    private final ConfigurationLoader configLoader;
//    private final JsonPersistenceManager persistenceManager;
    private final DataPersistenceManager dataPersistenceManager;

    private final UserRepositoryImpl userRepository;
    private final ChannelRepositoryImpl channelRepository;
    private final ParticipationRepositoryImpl participationRepository;
    private final ChannelMessageRepositoryImpl channelMessageRepository;
    private final DirectMessageRepositoryImpl directMessageRepository;
    private final UserServiceImpl userService;
    private final ChannelServiceImpl channelService;
    private final ParticipationServiceImpl participationService;
    private final ChannelMessageServiceImpl channelMessageService;
    private final DirectMessageServiceImpl directMessageService;


    public AppConfigRegacy() {
        this.dataPersistenceManager = DataPersistenceManager.getInstance();


        // 2. Repository 생성
        this.userRepository = new UserRepositoryImpl();
        this.channelRepository = new ChannelRepositoryImpl();
        this.participationRepository = new ParticipationRepositoryImpl();
        this.channelMessageRepository = new ChannelMessageRepositoryImpl();
        this.directMessageRepository = new DirectMessageRepositoryImpl();

        // 3. 데이터 로드
        loadAllData();

        // 4. Service 생성 및 의존성 주입
        this.userService = new UserServiceImpl(userRepository, participationRepository, channelMessageRepository, directMessageRepository);
        this.channelService = new ChannelServiceImpl(channelRepository);
        this.participationService = new ParticipationServiceImpl(participationRepository, userRepository, channelRepository);
        this.channelMessageService = new ChannelMessageServiceImpl(channelMessageRepository, participationRepository);
        this.directMessageService = new DirectMessageServiceImpl(directMessageRepository, userRepository);


    }

    private void loadAllData() {
        userRepository.loadDataMap(dataPersistenceManager.loadData(DataKey.USER));
        channelRepository.loadDataMap(dataPersistenceManager.loadData(DataKey.CHANNEL));
        participationRepository.loadDataMap(dataPersistenceManager.loadData(DataKey.PARTICIPATION));
        channelMessageRepository.loadDataMap(dataPersistenceManager.loadData(DataKey.CHANNEL_MESSAGE));
        directMessageRepository.loadDataMap(dataPersistenceManager.loadData(DataKey.DIRECT_MESSAGE));
        System.out.println("모든 데이터를 파일로부터 로드했습니다.");
        System.out.println("유저 수 : " + userRepository.getDataMap().size());
    }

    public void saveAllData() {
        dataPersistenceManager.saveData(DataKey.USER, userRepository.getDataMap());
        dataPersistenceManager.saveData(DataKey.CHANNEL, channelRepository.getDataMap());
        dataPersistenceManager.saveData(DataKey.PARTICIPATION, participationRepository.getDataMap());
        dataPersistenceManager.saveData(DataKey.CHANNEL_MESSAGE, channelMessageRepository.getDataMap());
        dataPersistenceManager.saveData(DataKey.DIRECT_MESSAGE, directMessageRepository.getDataMap());
        System.out.println("모든 데이터를 파일에 저장했습니다.");
    }

    // --- Service Getters ---
    public UserServiceImpl getUserService() { return userService; }
    public ChannelServiceImpl getChannelService() { return channelService; }
    public ParticipationServiceImpl getParticipationService() { return participationService; }
    public ChannelMessageServiceImpl getChannelMessageService() { return channelMessageService; }
    public DirectMessageServiceImpl getDirectMessageService() { return directMessageService; }

    /**
     * UI 계층에서 설정 파일에 접근할 수 있도록 ConfigurationLoader를 제공합니다.
//     */
//    public ConfigurationLoader getConfigLoader() {
//        return configLoader;
//    }
}