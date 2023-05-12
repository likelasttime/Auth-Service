# [완료] Auth-Service
[![image](https://github.com/likelasttime/Auth-Service/assets/46569105/755cddd2-0164-4a73-8a76-0e8d80857e62)](https://sonarcloud.io/project/overview?id=likelasttime_Auth-Service)  
JWT를 이용한 인증 서비스 개인 프로젝트  
MSA 아키텍처(📁auth-server 📁eureka-server 📁front-server 📁gateway 📁user-server)  
Monolithic 아키텍처(📁 src)

<br>

🟥 MSA 인증 서버 실행 영상  
https://youtu.be/ARxAACi77Lw  

📘기록 일지  
https://desert-echidna-283.notion.site/Auth-Service-b3dd5d28044541e6a8e520f7d1f1abaa  

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
|POST|/user-server/auth/signup |회원 가입|
|POST|/user-server/auth/verify|인증 메일 전송|
|POST|/user-server/auth/verify/{key}|인증 메일 검증|
|PUT|/user-server/auth/password|비밀번호 변경|
|POST|/user-server/auth/password/{key}|새 비밀번호 설정 URL 검증|
|POST|/user-server/auth/password|새 비밀번호 설정 메일 발송|
|GET|/user-server/user/email/{username}|사용자가 가입한 이메일 찾기|
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
