# 로그인 기능을 스프링 시큐리티 대신에 인터셉터로 교체했습니다.
## 변경점

- 기존에 사용하던 UserSecurityService와 SecurityConfig를 지우고, UserInterceptor와 WebConfig를 추가했습니다.
- 로그인 알고리즘을 UserController에 구현했습니다.
- @PreAuthorize를 사용하지 않고, Principal 대신 Session을 사용해 username을 저장하고 전달합니다.
- sec:authorize를 사용하지 않고, session을 통해 권한이 있는 유저인지 확인합니다.
- article_form.html의 _csrf를 대신해 Request와 Model과 Session을 사용해 csrfToken을 직접 만들어 사용했습니다.

### WebConfig
![image](https://github.com/user-attachments/assets/e294340a-9dba-4c2e-b7a8-0f7863271de9)

### UserInterceptor
![image](https://github.com/user-attachments/assets/4c560bfd-7816-4c37-be3d-4f771a32eea6)

### UserController의 로그인/로그아웃
![image](https://github.com/user-attachments/assets/0c9d98d3-9ebf-4a81-9a6d-148e08566615)

### @PreAuthorize를 지우고 Principal 대신 Session을 통해 유저확인
![image](https://github.com/user-attachments/assets/e10c26d3-3839-4db2-b45c-f250048605b8)

### sec:authorize 대신 session 사용
![image](https://github.com/user-attachments/assets/1b619ab8-6204-4ea8-af95-dbbb0178c5a0)

### csrfToken 생성
![image](https://github.com/user-attachments/assets/01f36bb0-c8a3-4248-a7ef-d860907f5aa0)

### csrfToken 검증
![image](https://github.com/user-attachments/assets/6fbfb242-9ec7-4746-875f-32902bb0d9ec)

### html에서 csrfToken 사용
![image](https://github.com/user-attachments/assets/3386ec34-81fd-4db4-880e-9390888c352d)
