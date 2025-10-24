package com.sprint.mission.discodeit.utils;

import com.sprint.mission.discodeit.data.DataKey;
import com.sprint.mission.discodeit.data.DataPersistenceManager;
import com.sprint.mission.discodeit.repository.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    @Bean
    public Scanner sc(){
        return new Scanner(System.in);
    }

}
