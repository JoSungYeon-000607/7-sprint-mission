package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.DirectMessage;
import com.sprint.mission.discodeit.repository.DirectMessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.DirectMessageService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

public class JCFDirectMessageService extends JCFBaseService<DirectMessage, UUID, DirectMessageRepository> implements DirectMessageService {

    private final DirectMessageRepository directMessageRepository;
    private final UserRepository userRepository; // 사용자 존재 여부 확인을 위해 추가

    public JCFDirectMessageService(DirectMessageRepository directMessageRepository, UserRepository userRepository) {
        super(directMessageRepository);
        this.directMessageRepository = directMessageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public DirectMessage sendMessage(UUID senderId, UUID receiverId, String message) {
        // 1. 비즈니스 규칙 검증: 보내는 사람과 받는 사람이 실제로 존재하는 사용자인지 확인
        if (!userRepository.existsByIdNonDel(senderId)) {
            throw new NoSuchElementException("메시지를 보내는 사용자를 찾을 수 없습니다: " + senderId);
        }
        if (!userRepository.existsByIdNonDel(receiverId)) {
            throw new NoSuchElementException("메시지를 받는 사용자를 찾을 수 없습니다: " + receiverId);
        }

        // 2. 엔티티 생성 위임
        DirectMessage newDirectMessage = DirectMessage.create(senderId, receiverId, message);

        // 3. 데이터 저장
        directMessageRepository.save(newDirectMessage);
        return newDirectMessage;
    }

    @Override
    public List<DirectMessage> getMessagesByReceiver(UUID receiverId) {
        return directMessageRepository.findByReceiverId(receiverId);
    }

    @Override
    public List<DirectMessage> getMessagesBySender(UUID senderId) {
        return directMessageRepository.findBySenderId(senderId);
    }

    @Override
    public List<DirectMessage> getConversation(UUID userOneId, UUID userTwoId) {
        return directMessageRepository.findByParticipants(userOneId, userTwoId);
    }
}