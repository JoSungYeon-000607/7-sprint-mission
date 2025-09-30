package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.State;
import com.sprint.mission.discodeit.entity.User;

import java.util.*;
import java.util.stream.Collectors;

/**
 * User 엔티티에 대한 데이터 영속성을 처리하는 Repository입니다.
 * 공통 CRUD 기능은 BaseRepository로부터 상속받습니다.
 */
public class UserRepository extends BaseRepository<User, UUID> {

    /**
     * 사용자 이름으로 사용자를 조회합니다.
     * @param username 조회할 사용자 이름
     * @return Optional<User> 조회된 사용자, 없으면 Optional.empty()
     */
    public Optional<User> findByUsername(String username){
        // Map의 모든 값을 순회하며 이름이 일치하는 첫 번째 사용자를 찾습니다.
        return dataMap.values().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst();
    }

    /**
     * 해당 사용자 이름이 이미 존재하는지 확인합니다.
     * @param username 확인할 사용자 이름
     * @return boolean 존재하면 true, 아니면 false
     */
    public boolean existsByUsername(String username){
        // findFirst()와 달리, 일치하는 데이터가 있는지 여부만 빠르게 확인하므로 더 효율적입니다.
        return dataMap.values().stream()
                .anyMatch(user -> user.getUsername().equals(username));
    }
}