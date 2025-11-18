package com.sprint.mission.discodeit.config.jwt;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class JwtAuthInterceptor implements HandlerInterceptor {

  private final JwtTokenProvider jwtTokenProvider;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    // -------------------------
    // OPTIONS 요청은 CORS preflight 요청이므로 인증 처리를 하지 않고 통과시킵니다.
    if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
      return true;
    }

    // === 핵심 분기 로직: 회원가입(POST /api/users) 요청은 인증 검사 없이 통과 ===
    if ("POST".equalsIgnoreCase(request.getMethod()) && "/api/users".equals(
        request.getRequestURI())) {
      System.out.println("분기 됨");
      return true;
    }

    // 1. Authorization 헤더에서 토큰 추출
    String token = resolveToken(request);

    // 2. 토큰 유효성 검사
    if (StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)) {
      // 3. 토큰이 유효하면, 토큰에서 사용자 ID를 추출하여 request에 속성으로 저장
      Claims claims = jwtTokenProvider.getClaims(token);
      String userId = claims.getSubject();
      request.setAttribute("userId", userId); // 컨트롤러에서 사용할 수 있도록 저장
      return true; // 요청 계속 진행
    }

    // 4. 토큰이 없거나 유효하지 않으면 401 Unauthorized 에러 응답 후 요청 중단
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않은 토큰입니다.");
    return false;
  }

  private String resolveToken(HttpServletRequest request) {
    String bearerToken = request.getHeader("Authorization");
    if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
      return bearerToken.substring(7);
    }
    return null;
  }
}
