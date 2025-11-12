package com.sprint.mission.discodeit.channel;

import com.sprint.mission.discodeit.common.repository.impl.BaseRepositoryImpl;
import com.sprint.mission.discodeit.config.enums.DataKey;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class ChannelRepositoryImpl extends BaseRepositoryImpl<Channel, UUID> implements
    ChannelRepository {

  /**
   * {@inheritDoc}
   * <p>
   * <b>[성능 주의]</b> 이 메서드는 저장된 모든 채널 데이터를 순회(Full Scan)하여
   * 이름이 일치하는 첫 번째 채널을 찾습니다. 시간 복잡도는 O(n)으로, 채널 수가 많아지면 성능 저하의 원인이 될 수 있습니다.
   */
  @Override
  public Optional<Channel> findByChannelName(String name) {
    // dataMap에 있는 모든 Channel 객체를 순회합니다.
    return dataMap.values().stream()
        // stream의 filter를 사용하여 이름이 일치하는 채널을 찾습니다.
        .filter(channel -> channel.getChannelName().equals(name))
        // 가장 먼저 찾은 1개를 반환합니다.
        .findFirst();
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>[성능 주의]</b> 이 메서드는 저장된 모든 채널 데이터를 순회(Full Scan)하여
   * 채널 이름의 존재 여부를 확인합니다. 시간 복잡도는 O(n)이며, {@code findFirst}보다 최적화되어 있지만 여전히 채널 수에 비례하여 시간이 소요됩니다.
   */
  @Override
  public boolean existsByChannelName(String name) {
    // dataMap에 있는 모든 Channel 객체를 순회하며,
    // anyMatch를 사용하여 이름이 일치하는 것이 하나라도 있는지 확인합니다.
    return dataMap.values().stream()
        .anyMatch(channel -> channel.getChannelName().equals(name));
  }


  @Override
  public List<Channel> findAllChannelsBySettings(String channelName, String description) {
    // 1. 저장소의 모든 채널 데이터를 스트림으로 변환합니다.
    Stream<Channel> channelStream = findAll().stream();

    // 2. [조건부 필터링] channelName 파라미터가 유효한 경우, 이름에 해당 문자열이 포함된 채널만 필터링합니다.
    if (channelName != null && !channelName.isBlank()) {
      channelStream = channelStream.filter(channel ->
          // 대소문자 구분 없이 검색하기 위해 모두 소문자로 변경하여 비교합니다.
          channel.getChannelName().toLowerCase().contains(channelName.toLowerCase())
      );
    }

    // 5. 모든 필터링을 거친 최종 결과를 List 형태로 수집하여 반환합니다.
    return channelStream.collect(Collectors.toList());
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>[성능 주의]</b> 이 메서드는 저장된 모든 채널 데이터를 순회(Full Scan)하여
   * 이름이 일치하는 첫 번째 채널을 찾습니다. 시간 복잡도는 O(n)으로, 채널 수가 많아지면 성능 저하의 원인이 될 수 있습니다.
   */
  @Override
  public Optional<Channel> findByChannelNameNonDel(String name) {
    // dataMap에 있는 모든 Channel 객체를 순회합니다.
    return dataMap.values().stream()
        // stream의 filter를 사용하여 이름이 일치하고 논리적 삭제가 되지 않은 채널을 찾습니다.
        .filter(channel -> !channel.isDeleted() && channel.getChannelName().equals(name))
        // 가장 먼저 찾은 1개를 반환합니다.
        .findFirst();
  }

  /**
   * {@inheritDoc}
   * <p>
   * <b>[성능 주의]</b> 이 메서드는 저장된 모든 채널 데이터를 순회(Full Scan)하여
   * 채널 이름의 존재 여부를 확인합니다. 시간 복잡도는 O(n)이며, {@code findFirst}보다 최적화되어 있지만 여전히 채널 수에 비례하여 시간이 소요됩니다.
   */
  @Override
  public boolean existsByChannelNameNonDel(String name) {
    // dataMap에 있는 모든 Channel 객체를 순회하며,
    // anyMatch를 사용하여 이름이 일치하고 논리적 삭제가 되지 않은 채널이 하나라도 있는지 확인합니다.
    return dataMap.values().stream()
        .anyMatch(channel -> !channel.isDeleted() && channel.getChannelName().equals(name));
  }
  

  @Override
  public DataKey getDataKey() {
    return DataKey.CHANNEL;
  }
}