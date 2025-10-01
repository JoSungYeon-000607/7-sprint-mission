package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Participation;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ParticipationRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ParticipationService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.UUID;

public class JCFParticipationService extends JCFBaseService<Participation, UUID, ParticipationRepository> implements ParticipationService {
    private final ParticipationRepository repository;
    private final UserService userService;
    private final ChannelService channelService;
    protected JCFParticipationService(ParticipationRepository repository, ChannelService channelService, UserService userService) {
        super(repository);
        this.repository = repository;
        this.userService = userService;
        this.channelService = channelService;
    }

    @Override
    public Participation joinChannel(UUID channelId, UUID userId, String nickname) {
        this.userAndChannelCheck(channelId, userId);
        if(isUserInChannel(channelId, userId)){
            throw new IllegalArgumentException("이미 채널에 참가 되어있습니다.");
        }
        if(nickname == null||nickname.isBlank()||nickname.length()>15){
            nickname = userService.findById(userId).getNickname();
        }
        Participation participation = Participation.create(channelId, userId, nickname, Role.USER);
        repository.save(participation);
        return participation;
    }

    @Override
    public void leaveChannel(UUID channelId, UUID userId) {
        this.userAndChannelCheck(channelId, userId);
        if(!isUserInChannel(channelId, userId)){
            throw new IllegalArgumentException("채널에 참여 되어 있지 않습니다.");
        }
        deleteById(findParticipationByChannelIdAndUserId(channelId, userId).getId());
    }

    @Override
    public Participation findParticipationByChannelIdAndUserId(UUID channelId, UUID userId) {
        return null;
    }

    @Override
    public void kickUserFromChannel(UUID channelId, UUID userIdToKick, UUID adminUserId) {
        if(channelId == null){
            throw new IllegalArgumentException("해당 채널을 찾을 수 없습니다.");
        }
        if(userIdToKick == null||adminUserId == null){
            throw new IllegalArgumentException("해당 사용자을 찾을 수 없습니다.");
        }
        if(!isOwner(adminUserId)){
            throw new IllegalArgumentException("권한이 없습니다.");
        }
        if(!isUserInChannel(userIdToKick, channelId)){
            throw new IllegalArgumentException("채널에서 해당 유저를 찾을 수 없습니다.");
        }
    }

    @Override
    public List<Participation> findParticipationsByChannelId(UUID channelId) {
        return List.of();
    }

    @Override
    public User findOwner(UUID channelId) {
        return null;
    }
    @Override
    public boolean isOwner(UUID uuid) {
        return false;
    }

    @Override
    public List<Participation> findParticipationsByUserId(UUID userId) {
        return List.of();
    }

    @Override
    public boolean isUserInChannel(UUID channelId, UUID userId) {
        return false;
    }

    @Override
    public void changeRole(UUID channelId, UUID userId, Role newRole) {

    }

    @Override
    public void changeNickname(UUID channelId, UUID userId, String newNickname) {

    }


    private void userAndChannelCheck(UUID channelId, UUID userId){
        if(userId == null){
            throw new IllegalArgumentException("사용자 정보를 가져올 수 없습니다.");
        }
        if(userService.existsById(userId)){

            throw new IllegalArgumentException("탈퇴한 사용자입니다.");
        }
        if(channelId == null){
            throw new IllegalArgumentException("채널 정보를 가져올 수 없습니다.");
        }
        if(channelService.existsById(channelId)){
            throw new IllegalArgumentException("삭제된 채널입니다.");
        }
    }
}
