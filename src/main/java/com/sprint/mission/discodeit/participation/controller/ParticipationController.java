package com.sprint.mission.discodeit.participation.controller;

import com.sprint.mission.discodeit.application.dto.SimpleChannel;
import com.sprint.mission.discodeit.application.service.UserManagementService;
import com.sprint.mission.discodeit.participation.ParticipationService;
import com.sprint.mission.discodeit.participation.dto.ParticipationRequestDTO;
import com.sprint.mission.discodeit.participation.dto.ParticipationResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/readStatuses")
@RequiredArgsConstructor
public class ParticipationController {

  private final ParticipationService participationService;
  private final UserManagementService userManagementService;

 
}
