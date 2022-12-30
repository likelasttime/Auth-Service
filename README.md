# [완료] Auth-Service
JWT를 이용한 인증 서비스 개인 프로젝트  
MSA 아키텍처(📁auth-server 📁eureka-server 📁front-server 📁gateway 📁user-server)  
Monolithic 아키텍처(📁 src)

<br>

### 코드 중 확인받고 싶은 부분
1. MSA 아키텍처
  - 더 나눠야 하거나 잘못 나눠진 부분이 있나요?  
  - 프런트엔드만 서버를 따로 두었는데 관련 파일이 별로 없으면 생성하지 않는 게 나을까요?  
2. 테스트 코드  
    - /src/test/java/winterdevcamp/Auth/Service/
    - 테스트 코드에서 보완점을 알고 싶습니다.
3. /gateway/src/main/java/com/example/gateway/filter/CustomAuthFilter.java에서 apply 메소드
    - Header에서 Authorization을 가져오는 대신 Cookie에서 AccessToken을 가져오도록 구현했습니다. 헤더의 Authorization에 토큰을 넣는 게 더 나을까요? ➡️  Authorization에 토큰을 넣는 방식으로 바꿀 예정입니다.  
4. URL 설계
    - /user-server/user/signup 이런 식으로 /인스턴스명/행위의 대상/행위로 했습니다.
      - 외부에 구조가 다 노출되는 것 같은데 route 하기 전에 임의의 path 설정할까요?
5. /src/main/java/winterdevcamp/Auth/Service/controller/HomeController.java
    - updateInfo 메소드
        - 단순히 view만 반환하지 않고 개인 정보까지 설정해서 보내고 있습니다. MSA에서는 문제가 생기네요. 두 기능을 분리하는 게 더 옳은가요?
          - GET을 요청하면 view만 리턴하고 비동기로 개인 정보를 가져와서 출력하는 방법을 생각하고 있습니다.

<br>

### 궁금한 점  
1. PK를 auto-increment로 해서 사용하면 보안 측면에서 문제가 있다는 것을 알고 있습니다. UUIDv4를 사용하는 글을 읽었는데 실제로는 어떤 방법을 주로 사용하는지 궁금합니다.  
2. BCrypt는 RAM에 많은 양을 요구한다는 말이 있던데 그러면 자주 사용되는 방식이 무엇인가요?
3. 저는 다 구현한 후에 MSA를 적용해봤는데 처음부터 MSA 구조로 시작해야 하나요?
4. 팀 프로젝트 때 Spring Data JPA는 가급적 사용하지 말라고 했는데 MyBatis 쓰면 되나요?
5. 회원 탈퇴를 구현할 때 아예 삭제해도 되나요? 서비스를 사용하다 보면 다음에 같은 계정으로 다시 가입할 수 없다는 방식을 많이 봤습니다. 삭제된 회원은 따로 보관을 해야 하나요?
6. 회원 정보를 수정할 때 횟수에 제한이 있도록 구현을 하나요?
7. 유저 관리처럼 관리자가 사용할 수 있는 기능은 권한이 없는 사용자에게 안 보여지는 게 나을까요? 아니면 접근을 시도하면 안 된다는 알림을 띄울까요?
8. Member 테이블에 언제 최종 접근했는지 알 수 있는 필드를 추가한다면 로그인 할때마다 갱신하나요?  
9. 권한을 0==가입신청, 1==이메일인증, 2==정회원, 3==게시판 관리자처럼 나타내는 것을 봤습니다. 저는 ROLE_USER와 같이 사용하고 있는데 두 가지 방식 다 테이블 필드에 들어가야 하나요? 아니면 선택인가요?

<br>  

### 기술스택
🟧 <b>Backend</b>  
- Java 11  
- Spring Boot 2.6.14  
- MySQL  
- Spring Data JPA  
- Redis  
- Junit5  
- H2 
- Spring Cloud 2021.0.0  
  - Eureka
  - Spring Cloud Gateway

<br>

🟨 <b>Frontend</b>  
- HTML  
- CSS  
- JavaScript  
- JQuery  
- BootStrap  
- Thymeleaf 

<br>

## MSA 아키텍처  
![msa 아키텍처](https://user-images.githubusercontent.com/46569105/209476749-5b506a88-0ce1-40f4-8c62-50e9cf0136b3.jpg)

*eureka-server* : 인스턴스들의 정보를 가지고 있는 서버  
*gateway* : 라우팅, 토큰 검증  
*user-server* : 사용자와 관련된 다양한 서비스(회원가입, 유저 관리 등)  
*auth-server* : 로그인 및 토큰 생성  
*front-server* : view  


<br>

## ERD
![erd](https://user-images.githubusercontent.com/46569105/207863286-00389f74-14f6-4091-a6e7-da4b1d03a877.png)

<br>

## 기능
- 가입, 로그인
  - 로그인에 성공하면 JWT 발급
    - Access Token, Refresh Token은 Cookie에 저장
- 유저 관리 페이지
  - 관리자 권한을 가진 사람만 인가
  - NOT_PERMITTED가 아닌 회원들만 조회(USER, ADMIN, MANAGER)
- 인증 서버 API
  - 세션 대신에 JWT로 판단하도록 CustomFilter 생성
    - CustomFilter는 UsernamePasswordAuthenticationFilter 앞단에 위치
- RDBMS 사용(MySQL)
- Password Encryption
  - 랜덤 값 salt를 추가해서 해쉬 함수 생성
  - salt는 회원 테이블과 1:1 매핑으로 SALT 테이블에 저장
  - BCrypt 사용
- 비밀번호 찾기
  - 새로운 비밀번호를 설정할 수 있는 링크를 담아 이메일을 전송
- 캐시
  - Refresh Token 저장
  - 이메일 인증, 비밀번호 찾기 메일 URL 파라미터 key를 저장
    - 해당 URL은 임의의 시간 동안만 유효함
- 이메일 인증
  - 로그인 시 NOT_PERMITTED 유저라면 이메일 인증 페이지로 이동

<br>

## API 명세  
|Method|URI|설명|
|---|---|----|
|POST|/auth-server/auth/login|로그인|
|POST|/user-server/user/signup |회원 가입|
|POST|/user-server/user/verify|인증 메일 전송|
|POST|/user-server/user/verify/{key}|인증 메일 검증|
|PUT|/user-server/user/password|비밀번호 변경|
|POST|/user-server/user/password/{key}|새 비밀번호 설정 URL 검증|
|POST|/user-server/user/password|새 비밀번호 설정 메일 발송|
|PUT|/user-server/user/info|개인 정보 수정|
|POST|/user-server/user/remove|탈퇴|
|GET|/user-server/admin/manage|사용자 관리(조회)|

<br>

## 📸 Screenshot
✔️ <b>메인</b>
![main](https://user-images.githubusercontent.com/46569105/209480347-f0168ab9-e33b-4d62-aaac-056e2266cd7e.png)
✔️ <b>회원 가입</b>
![회원가입](https://user-images.githubusercontent.com/46569105/208302487-dbfba85f-1070-45ab-82db-0798db8b17d4.png)
✔️ <b>로그인</b>
![로그인](https://user-images.githubusercontent.com/46569105/208302647-ffb5d79c-bd4a-49bb-ae99-6cfd869a81c2.png)
✔️ <b>비밀번호 찾기</b>
![비밀번호 찾기](https://user-images.githubusercontent.com/46569105/208302597-2076778c-fa06-4c96-a0fc-0bb4c9cf307a.png)
✔️ <b>사용자 관리</b>
![마킹 처리 user management page](https://user-images.githubusercontent.com/46569105/208302839-18c6865e-f7c4-486a-a6f5-5290dc651127.png)
