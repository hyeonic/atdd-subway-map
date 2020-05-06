## 1단계 - 지하철 노선 / 인수 테스트
### 요구 사항
- [ ] 인수 테스트(LineAcceptanceTest) 성공 시키기
- [ ] LineController를 구현하고 인수 테스트에 맞는 기능을 구현하기
- [ ] 테스트의 중복을 제거하기
### 제약 사항
- 지하철 노선 이름은 중복될 수 없다.
### 시나리오
```
Feature: 지하철 노선 관리
  Scenario: 지하철 노선을 관리한다.
    When 지하철 노선 n개 추가 요청을 한다.
    When 지하철 노선 목록 조회 요청을 한다.
    Then 지하철 노선 목록을 응답 받는다.
    And 지하철 노선 목록은 n개이다.
    When 지하철 노선 수정 요청을 한다.
    Then 지하철 노선이 수정 되었다.
    When 지하철 노선 제거 요청을 한다.
    Then 지하철 노선이 제거 되었다.
    When 지하철 노선 목록 조회 요청을 한다.
    Then 지하철 노선 목록을 응답 받는다.
    And 지하철 노선 목록은 n-1개이다.
```