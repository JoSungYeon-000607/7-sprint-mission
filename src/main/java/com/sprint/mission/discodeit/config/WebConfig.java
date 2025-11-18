package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.config.jwt.JwtAuthInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

  private final JwtAuthInterceptor jwtAuthInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(jwtAuthInterceptor)
        .addPathPatterns("/api/**")
        .excludePathPatterns(
            "/api/auth/login",
            "/api/users/all"
        );
  }
}
