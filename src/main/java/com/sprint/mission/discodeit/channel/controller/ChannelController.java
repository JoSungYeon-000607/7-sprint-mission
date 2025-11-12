package com.sprint.mission.discodeit.channel.controller;

import com.sprint.mission.discodeit.channel.ChannelService;
import com.sprint.mission.discodeit.channel.dto.ChannelRequestDTO;
import com.sprint.mission.discodeit.channel.dto.ChannelResponseDTO;
import com.sprint.mission.discodeit.config.enums.Role;
import com.sprint.mission.discodeit.config.enums.Type;
import com.sprint.mission.discodeit.participation.ParticipationService;
import com.sprint.mission.discodeit.participation.dto.ParticipationRequestDTO;
import com.sprint.mission.discodeit.user.User;
import com.sprint.mission.discodeit.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/channels")
@RequiredArgsConstructor
public class ChannelController {

  private final ChannelService channelService;
  private final ParticipationService participationService;
  private final UserService userService; // UserService 주입

  @PostMapping("/public")
  public ResponseEntity<ChannelResponseDTO> createChannel(
      @RequestAttribute("userId") String userIdString,
      @RequestBody @Valid ChannelRequestDTO requestDTO) {

    UUID userId = UUID.fromString(userIdString);
    User user = userService.findById(userId); // userId로 사용자 정보 조회

    ChannelResponseDTO channelDTO = channelService.create(requestDTO, Type.PUBLIC);

    ParticipationRequestDTO participationRequest = new ParticipationRequestDTO(
        channelDTO.id(),
        user.getUsername()
    );
    participationService.joinChannel(participationRequest, userId);

    return ResponseEntity.status(HttpStatus.CREATED).body(channelDTO);
  }


  @PostMapping("/changeSettings/{channelId}")
  public ResponseEntity<?> changeChannelSettings(
      @PathVariable UUID channelId,
      @RequestBody @Valid ChannelRequestDTO requestDTO,
      @RequestAttribute("userId") String userIdString) {

    channelService.changeSettings(channelId, requestDTO);

    ChannelResponseDTO responseDTO = ChannelResponseDTO.from(channelService.findById(channelId));

    return ResponseEntity.ok(responseDTO);
  }
}
