package com.sprint.mission.discodeit.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 설정 파일을 기반으로 데이터 경로를 관리하고, JSON 파일의 저장/로딩을 처리하는 클래스입니다.
 */
public class JsonPersistenceManager {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final ConfigurationLoader config;
    private final Path dataDirectoryPath;

    public JsonPersistenceManager() {
        // 1. 자신의 의존성인 ConfigurationLoader를 직접 생성.
        this.config = new ConfigurationLoader("config.properties");

        // 2. 애플리케이션 실행 위치(프로젝트 루트)를 기준으로 데이터 저장 폴더 경로를 설정.
        String projectRoot = System.getProperty("user.dir");
        String dataSubDir = config.getProperty("data.directory", "src/main/resources/data");
        this.dataDirectoryPath = Paths.get(projectRoot, dataSubDir);

        // 3. 데이터 저장 폴더를 생성.
        File dataDir = dataDirectoryPath.toFile();
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
    }

    /**
     * 설정 키를 받아 완전한 파일 경로를 반환.
     */
    private String getDataFilePath(String key) {
        String fileName = config.getProperty(key);
        return dataDirectoryPath.resolve(fileName).toString();
    }

    /**
     * 설정 키에 해당하는 JSON 파일을 읽어 Map 객체로 변환.
     */
    public <ID, T> Map<ID, T> loadData(String key, Type type) {
        String fullPath = getDataFilePath(key);
        if (!Files.exists(Paths.get(fullPath))) {
            return new ConcurrentHashMap<>();
        }
        try (FileReader reader = new FileReader(fullPath)) {
            Map<ID, T> data = gson.fromJson(reader, type);
            return data == null ? new ConcurrentHashMap<>() : new ConcurrentHashMap<>(data);
        } catch (IOException e) {
            // 초기 실행 시 파일이 없어서 발생하는 오류는 정상일 수 있으므로 메시지를 완화.
            // System.out.println("데이터 파일 없음, 새로 생성됩니다: " + fullPath);
            return new ConcurrentHashMap<>();
        }
    }

    /**
     * Map 데이터를 설정 키에 해당하는 JSON 파일로 저장.
     */
    public <ID, T> void saveData(String key, Map<ID, T> data) {
        String fullPath = getDataFilePath(key);
        try (FileWriter writer = new FileWriter(fullPath)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            System.err.println("데이터 저장 중 오류 발생: " + fullPath);
            e.printStackTrace();
        }
    }

    public String getDataDirectoryPath() {
        return dataDirectoryPath.toString();
    }
}