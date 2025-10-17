
## 요구사항

### 기본
- [x] 기본 항목 1
- [x] 기본 항목 2

### 심화
- [x] 심화 항목 1
- [x] 심화 항목 2

## 주요 변경사항
- Entity, Service, Repository 모두 공통 기능은 Base abstract class를 통해 통합 관리합니다. 
- 데이터는 DataPersistenceManager를 통해 관리하고 각 데이터는 DataKey enum을 통해 식별합니다.
- Participation은 다른 Entity와 다르게 UserId, ChannelId를 식별용으로 사용합니다.

## 스크린샷
![image](이미지url)

## 멘토에게
- Service에는 기능이 존재하지만 실제 테스트 했을 때 추가 기능이 필요해 사용이 어려운 기능이 존재합니다.
