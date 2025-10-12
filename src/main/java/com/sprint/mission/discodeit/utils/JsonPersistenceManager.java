package com.sprint.mission.discodeit.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 각 Repository의 dataMap을 JSON 파일로 저장하고 불러오는 영속성 관리 클래스입니다.
 */
public class JsonPersistenceManager {

    // Gson 인스턴스 생성. prettyPrinting()으로 가독성 좋은 JSON 파일을 만듭니다.
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * 제네릭 메서드로, 주어진 경로의 JSON 파일을 읽어 Map 객체로 변환합니다.
     *
     * @param path 읽어올 파일 경로
     * @param type 변환할 Map의 타입 정보 (e.g., new TypeToken<Map<UUID, User>>() {}.getType())
     * @param <ID> Map의 Key 타입
     * @param <T>  Map의 Value 타입 (엔티티)
     * @return 파일이 존재하면 데이터가 채워진 Map, 없으면 비어있는 ConcurrentHashMap 반환
     */
    public <ID, T> Map<ID, T> loadData(String path, Type type) {
        // 파일이 존재하지 않으면, 초기 실행으로 간주하고 비어있는 맵을 반환합니다.
        if (!Files.exists(Paths.get(path))) {
            return new ConcurrentHashMap<>();
        }
        try (FileReader reader = new FileReader(path)) {
            Map<ID, T> data = gson.fromJson(reader, type);
            // 파일은 존재하지만 내용이 비어있는 경우 null이 반환될 수 있으므로, 안전하게 처리합니다.
            return data == null ? new ConcurrentHashMap<>() : new ConcurrentHashMap<>(data);
        } catch (IOException e) {
            System.err.println("데이터 로딩 중 오류 발생: " + path);
            e.printStackTrace();
            // 로딩 중 에러가 발생하면, 데이터 유실을 막기 위해 비어있는 맵을 반환합니다.
            return new ConcurrentHashMap<>();
        }
    }

    /**
     * 제네릭 메서드로, 주어진 Map 데이터를 지정된 경로의 JSON 파일로 저장합니다.
     *
     * @param path 저장할 파일 경로
     * @param data 저장할 데이터 Map
     * @param <ID> Map의 Key 타입
     * @param <T>  Map의 Value 타입 (엔티티)
     */
    public <ID, T> void saveData(String path, Map<ID, T> data) {
        try (FileWriter writer = new FileWriter(path)) {
            gson.toJson(data, writer);
        } catch (IOException e) {
            System.err.println("데이터 저장 중 오류 발생: " + path);
            e.printStackTrace();
        }
    }
}