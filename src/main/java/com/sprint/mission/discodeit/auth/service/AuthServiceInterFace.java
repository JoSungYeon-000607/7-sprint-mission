package com.sprint.mission.discodeit.auth.service;

import com.sprint.mission.discodeit.auth.AuthUser;

import java.util.Optional;

public interface AuthServiceInterFace {
    Optional<AuthUser> login(String username, String password);
    void logout();
    AuthUser getCurrentUser();
    boolean isLoggedIn();
}
