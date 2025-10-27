package com.sprint.mission.discodeit.message.direct;

import com.sprint.mission.discodeit.common.service.impl.BaseServiceImpl;
import com.sprint.mission.discodeit.config.exception.UserNotFoundException;
import com.sprint.mission.discodeit.user.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
public class DirectMessageServiceImpl extends BaseServiceImpl<DirectMessage, UUID, DirectMessageRepository> implements DirectMessageService {// 사용자 존재 여부 확인을 위해 추가
    private final UserService userService;

    public DirectMessageServiceImpl(DirectMessageRepository directMessageRepository, UserService userService) {
        super(directMessageRepository);
        this.userService = userService;
    }

    @Override
    public DirectMessage sendMessage(UUID senderId, UUID receiverId, String message) {
        // 1. 비즈니스 규칙 검증: 보내는 사람과 받는 사람이 실제로 존재하는 사용자인지 확인
        if (!userService.existsByIdNonDel(senderId)) {
            throw new UserNotFoundException(senderId);
        }
        if (!userService.existsByIdNonDel(receiverId)) {
            throw new UserNotFoundException(receiverId);
        }

        // 2. 엔티티 생성 위임
        DirectMessage newDirectMessage = DirectMessage.create(senderId, receiverId, message);

        // 3. 데이터 저장
        save(newDirectMessage);
        return newDirectMessage;
    }

    @Override
    public List<DirectMessage> getMessagesByReceiver(UUID receiverId) {
        return repository.findByReceiverId(receiverId);
    }

    @Override
    public List<DirectMessage> getMessagesBySender(UUID senderId) {
        return repository.findBySenderId(senderId);
    }

    @Override
    public List<DirectMessage> getConversation(UUID userOneId, UUID userTwoId) {
        return repository.findByParticipants(userOneId, userTwoId);
    }

    @Override
    public void delAllBySenderId(UUID senderId) {
        if(repository.findBySenderId(senderId).isEmpty()){
            throw new NoSuchElementException("사용자가 보낸 개인 메새지가 없습니다.");
        }
        repository.deleteAllBySenderId(senderId);
    }
}