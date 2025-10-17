# DiscodeIt - 콘솔 기반 채팅 애플리케이션

## 1. 프로젝트 소개

DiscodeIt은 Java로 구현된 콘솔 기반의 실시간 채팅 애플리케이션입니다. 사용자 관리, 채널 생성 및 참여, 채널 메시지 및 1:1 다이렉트 메시지(DM) 기능을 제공합니다. 객체 지향 설계 원칙과 계층형 아키텍처를 적용하여 코드의 재사용성과 유지보수성을 높이는 데 중점을 두었습니다.

## 2. 주요 기능

* **사용자 관리**:
    * 사용자 생성, 프로필(닉네임, 이메일) 수정
    * 비밀번호 변경 (8자 이상)
    * 사용자 상태 변경 (온라인, 오프라인, 자리비움, 다른 용무 중)
    * 논리적 삭제(Soft Delete) 및 복구, 물리적 삭제(Hard Delete)
    * 사용자 이름으로 검색 및 전체/활성 사용자 목록 조회
* **채널 관리**:
    * 채널 생성 (이름 필수, 타입/주제/공개여부 설정 가능)
    * 채널 설정 변경 (이름, 타입, 주제, 공개여부)
    * 논리적/물리적 채널 삭제
    * 채널 이름 또는 다양한 조건(타입, 주제 포함)으로 검색
* **참여 관리**:
    * 채널 참여 및 나가기 (참여 시 채널별 닉네임 설정 가능)
    * 채널 내 닉네임 변경
    * 관리자 기능: 참여자 강퇴, 역할 변경(ADMIN, USER, GUEST)
    * 특정 채널의 참여자 목록 또는 특정 사용자의 참여 채널 목록 조회
* **메시지 관리**:
    * 채널 메시지 보내기, 조회, 논리적 삭제
    * DM(1:1 메시지) 보내기, 대화 내용 조회, 물리적 삭제
* **데이터 영속성**:
    * 애플리케이션 종료 시 모든 데이터(사용자, 채널, 참여 정보, 메시지)를 파일(`.txt`)에 자동으로 저장.
    * 애플리케이션 시작 시 파일로부터 데이터를 로드.

## 3. 기술 스택 및 주요 설계

* **언어**: Java 17
* **빌드 도구**: Gradle
* **테스트**: JUnit 5, AssertJ
* **데이터 저장**: Java Serialization을 이용한 파일 저장 (`.txt`)
* **아키텍처**:
    * **계층형 아키텍처**: `View` - `Service` - `Repository` - `Entity` 계층으로 역할을 명확히 분리했습니다.
    * **인터페이스 기반 설계**: Service와 Repository 계층은 인터페이스를 정의하고, 메모리 기반 구현체(`JCF*`)를 구현하여 의존성을 낮추고 유연성을 확보했습니다.
    * **의존성 주입(DI)**: `AppConfig` 클래스가 객체 생성과 의존성 주입을 담당하여 객체 간 결합도를 낮췄습니다.
    * **공통 기능 추상화**: `BaseEntity`, `BaseService`, `BaseRepository`를 통해 공통 CRUD 및 관리 로직을 추상화하여 코드 중복을 최소화했습니다.
    * **UI 분리**: 콘솔 입출력 로직을 각 도메인별 `View` 클래스(`UserView`, `ChannelView` 등)로 분리하고, 공통 UI 로직은 `SharedView`로 추출했습니다.
* **데이터 관리**:
    * **싱글톤 패턴**: `DataPersistenceManager`는 싱글톤으로 구현되어 프로그램 전체에서 하나의 인스턴스만 사용됩니다.
    * **데이터 식별**: 저장할 데이터 종류는 `DataKey` Enum으로 관리됩니다.
    * **복합 키**: 채널 참여 정보(`Participation`)는 `ChannelId`와 `UserId`를 조합한 `ParticipationDualKey` 레코드를 복합 기본 키로 사용합니다.
    * **연쇄 삭제**: 사용자 물리적 삭제 시, 해당 사용자의 참여 정보, 채널 메시지, DM 기록이 함께 삭제됩니다.

## 4. 설치 및 실행 방법

1.  **소스 코드 클론**:
    ```bash
    git clone [저장소_URL]
    cd [프로젝트_폴더]
    ```
2.  **빌드**:
    ```bash
    ./gradlew build
    ```
    (Windows에서는 `gradlew.bat build`)
3.  **실행**:
    `JavaApplication.java` 파일의 `main` 메서드를 IDE에서 직접 실행하거나, 빌드된 jar 파일을 실행합니다.
    ```bash
    java -jar build/libs/[생성된_jar_파일_이름].jar
    ```
    (실행 가능한 jar 파일 생성을 위해서는 `build.gradle`에 `application` 플러그인 설정이 필요할 수 있습니다.)

## 5. 프로젝트 구조
    src
    ├── main
    │   ├── java
    │   │   └── com/sprint/mission/discodeit
    │   │       ├── data             # 데이터 영속성 관리 (DataPersistenceManager, DataKey)
    │   │       ├── entity           # 도메인 모델 (User, Channel, Message 등)
    │   │       ├── repository       # 데이터 접근 인터페이스 및 구현체 (UserRepository, JCFUserRepository 등)
    │   │       ├── service          # 비즈니스 로직 인터페이스 및 구현체 (UserService, JCFUserService 등)
    │   │       ├── view             # 콘솔 UI 관련 클래스 (UserView, ChannelView, SharedView 등)
    │   │       ├── utils            # 유틸리티 클래스 (AppConfig, DateUtils 등)
    │   │       └── JavaApplication.java # 메인 실행 클래스
    │   └── resources
    │       └── config.properties    # (참고용) 설정 파일
    └── test
        └── java                     # 단위 테스트 및 통합 테스트 코드
    build.gradle                     # Grad

## 6. 추가 정보

* 이 프로젝트는 Java Collections Framework(JCF)를 기반으로 한 메모리 데이터 저장 방식을 사용합니다. 애플리케이션이 종료되면 모든 변경 사항이 `data/` 폴더 내의 `.txt` 파일에 직렬화되어 저장됩니다.
* 엔티티 클래스(`User`, `Channel` 등)는 단순한 데이터 컨테이너(Data Transfer Object, DTO)가 아니라, 자신의 상태를 변경하는 비즈니스 로직(예: `user.goOnline()`, `channel.changeSettings()`)을 포함하는 풍부한 도메인 모델(Rich Domain Model)로 설계되었습니다.
