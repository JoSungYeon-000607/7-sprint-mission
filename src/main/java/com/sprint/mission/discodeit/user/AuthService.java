package com.sprint.mission.discodeit.user;

import com.sprint.mission.discodeit.user.dto.LoginRequestDTO;
import com.sprint.mission.discodeit.user.dto.LoginResponseDTO;
import com.sprint.mission.discodeit.user.dto.LoginTester;

public interface AuthService {

  LoginTester login(LoginRequestDTO request);
}
