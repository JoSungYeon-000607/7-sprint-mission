package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Identifiable;

import java.util.*;

import java.util.*;

/**
 * 모든 Repository의 공통 CRUD 기능을 제공하는 추상 클래스입니다.
 * 제네릭을 사용하여 어떤 타입의 엔티티든 처리할 수 있습니다.
 * @param <T> Identifiable 인터페이스를 구현한 엔티티 타입
 * @param <ID> 해당 엔티티의 ID 타입
 */
public abstract class BaseRepository<T extends Identifiable<ID>, ID> {
    // 각 하위 Repository 인스턴스가 자신만의 데이터 저장소를 갖도록 final 인스턴스 변수로 선언합니다.
    protected final Map<ID, T> dataMap = new HashMap<>();

    public void save(T entity) {
        dataMap.put(entity.getId(), entity);
    }

    public Optional<T> findById(ID id) {
        return Optional.ofNullable(dataMap.get(id));
    }

    public List<T> findAll() {
        // 내부 dataMap을 외부로 노출하지 않기 위해 방어적 복사(Defensive Copy)를 사용합니다.
        return new ArrayList<>(dataMap.values());
    }

    public void deleteById(ID id) {
        dataMap.remove(id);
    }

    public void deleteAll() {
        dataMap.clear();
    }

    public long count() {
        return dataMap.size();
    }

    public boolean existsById(ID id) {
        return dataMap.containsKey(id);
    }
}
