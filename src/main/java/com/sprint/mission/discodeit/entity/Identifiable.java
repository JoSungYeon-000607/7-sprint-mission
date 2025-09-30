package com.sprint.mission.discodeit.entity;

import java.util.UUID;

/**
 * 모든 엔티티가 ID를 가지고 있음을 명시하는 인터페이스입니다.
 * 제네릭 BaseRepository에서 엔티티의 타입을 제한하는 데 사용됩니다.
 * @param <ID> ID의 타입 (예: UUID, Long)
 */
public interface Identifiable<ID> {
    ID getId();
}
