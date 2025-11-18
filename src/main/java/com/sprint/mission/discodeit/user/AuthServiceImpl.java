package com.sprint.mission.discodeit.user;

import com.sprint.mission.discodeit.config.jwt.JwtTokenProvider;
import com.sprint.mission.discodeit.content.binary.BinaryContent;
import com.sprint.mission.discodeit.content.binary.BinaryContentService;
import com.sprint.mission.discodeit.user.dto.AuthUserDTO;
import com.sprint.mission.discodeit.user.dto.LoginRequestDTO;
import com.sprint.mission.discodeit.user.dto.LoginResponseDTO;
import com.sprint.mission.discodeit.user.dto.LoginTester;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtTokenProvider jwtTokenProvider;
  private final BinaryContentService binaryContentService;

  @Override
  public LoginTester login(LoginRequestDTO request) {
    User user = userRepository.findByUsername(request.username()).orElseThrow(
        () -> new NoSuchElementException("사용자를 찾을 수 없습니다.")
    );
    if (!passwordEncoder.matches(request.password(), user.getPassword())) {
      throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }
    UUID profileId = binaryContentService.findAllByOwnerId(user.getId()).get(1).id();

    return LoginTester.from(user, profileId);
  }
}

//String username = request.username();
//String password = request.password();
//User user = userRepository.findByUsername(username).orElseThrow(
//    () -> new NoSuchElementException("사용자를 찾을 수 없습니다."));
//
//    if (passwordEncoder.matches(password, user.getPassword())) {
//AuthUserDTO authUser = AuthUserDTO.from(user);
//String accessToken = jwtTokenProvider.createAccessToken(authUser);
//      return new LoginResponseDTO(accessToken);
//    } else {
//        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
//    }