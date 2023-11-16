# budget-buddy
예산 관리 및 지출 컨설팅 어플리케이션


## 목차
- [개요](#개요)
- [사용기술](#사용기술)
- [API](#API)
- [구현 기능](#구현기능)
- [시스템 구성도](#시스템-구성도)
- [ERD](#ERD)
- [TIL 및 회고](#프로젝트-관리-및-회고
  )


## 개요

본 서비스는 사용자가 `카테고리 및 예산`을 선정하고, 지출 기록에 따라 `일별 및 월별 예산`을 추천합니다 .<br>
`통계 조회 및 기록`을 통해 더 나은 사용자 예산 관리를 제공하고, 다른 사람의 예산 및 지출과 비교한 예산 추천을 받을 수 있습니다.<br>

## 프로젝트 일정
  ![image](https://github.com/12hyeon/budget-buddy/assets/67951802/858a66cb-8d48-467f-8097-a5205c3f34c9)

## 사용기술

#### 개발환경
<img src="https://img.shields.io/badge/java-007396?&logo=java&logoColor=white"> <img src="https://img.shields.io/badge/spring-6DB33F?&logo=spring&logoColor=white"> <img src="https://img.shields.io/badge/Spring boot-6DB33F?&logo=Spring boot&logoColor=white"> <img src="https://img.shields.io/badge/gradle-02303A?&logo=gradle&logoColor=white">
<br>
<img src="https://img.shields.io/badge/redis-DC382D?&logo=redis&logoColor=white"> <img src="https://img.shields.io/badge/Spring JPA-6DB33F?&logo=Spring JPA&logoColor=white"> 
<br>
<img src="https://img.shields.io/badge/AssertJ-25A162?&logo=AssertJ&logoColor=white"> <img src="https://img.shields.io/badge/Mockito-008D62?&logo=Mockito&logoColor=white">
<br>
<img src="https://img.shields.io/badge/intellijidea-000000?&logo=intellijidea&logoColor=white"> <img src="https://img.shields.io/badge/postman-FF6C37?&logo=postman&logoColor=white"> <img src="https://img.shields.io/badge/swagger-85EA2D?&logo=swagger&logoColor=white">


## API

  ![swagger](https://github.com/12hyeon/budget-buddy/assets/67951802/29e743ab-bd10-45f7-929b-682d8fa42dce)


## 구현기능

<details>
  <summary> 유저 </summary>

- **구현 기능** <br>
    - 회원가입, 로그인, RT 재발급

- **구현 방법** <br>
    - 회원가입: 사용자 회원 양식을 받아 DB에 저장하고, <br> 비밀번호에 대해 총 4가지 조건을 util로 적용</br>
    - 로그인: 사용자 로그인 양식을 받아 DB와 비교한 후, Access Token, Refresh Token 발급
    - RT 재발급: redis에 로그인에 따라 발급된 RT를 저장하고, AT 초과 시 redis에 존재하면 재발급하고 그렇지 않으면 재로그인 시도 요청을 전송
</details>

<details>
  <summary> 카테고리 </summary>

- **구현 기능** <br>
    - 카테고리 추가, 조회

- **구현 방법** <br>
    - 제한된 개수 이내의 카테고리 생성
    - 전체 카테고리 명과 id 조회
</details>

<details>
  <summary> 예산 및 지출 </summary>

- **구현 기능** <br>
    - 월별 예산 crud
    - 지출 crud

- **구현 방법** <br>
    - 예산은 당월에 동일한 카테고리별 예산의 유무 확인 후, 생성
    - 기간별, 금액 별, 카테고리별, page에 따라 내역 조회 가능
</details>

<details>
  <summary> ** 추천 </summary>

- **구현 기능** <br>
    - 카테고리별 당일 예산 추천 및 당월 예산 추천

- **구현 방법**<br>
    - 당일 예산은 자정까지 redis에 기록하여 빠른 조회 가능
    - 당월 추천은 본인의 일주일 지출, 모든 사용자의 평균 월별 지출에 따라 변경 

</details>
<details>
  <summary> ** 통계 </summary>

- **구현 기능** <br>
    - 사용자가 카테고리마다 일별 사용한 금액 기록

- **구현 방법**<br>
    - 달의 마지막 날이 지남에 따라 해당 월, 일의 금액을 누적하여 기록
    - 스케줄링을 통해서 당일, 당월 내역을 로깅 및 DB에 기록
  
</details>

## web hook
- discord와 연결하여, 실제 사용자가 매일 오전 8시에 알림이 가도록 구성
- 알림 : 카테고리별 당일 추천 예산 정보
  
  ![image](https://github.com/12hyeon/budget-buddy/assets/67951802/92daf134-980e-478b-aa03-d1c3077966f0)


## 시스템-구성도
  ![시스템_구성도](https://github.com/12hyeon/budget-buddy/assets/67951802/f04ba7c3-2ed6-4668-b5ad-fda98b982145)

## ERD
  ![ERD](https://github.com/12hyeon/budget-buddy/assets/67951802/b49d86cc-cad2-44b9-9aac-d824b685187d)

