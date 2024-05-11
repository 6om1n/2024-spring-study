# ⭐ 2. URI와 웹 브라우저 요청 흐름
## URI
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/fa712f1c-e893-4d26-a6c7-8663226d9eb9)

- URI (Uniform Resource Identifier) 는 로케이터(locator), 이름(name) 또는 둘 다 추가로 분류될 수 있다. 

#### URI 란 ?
- Uniform : 리소스 식별하는 통일된 방식
- Resource : 자원, URI로 식별할 수 있는 모든 것
- Identifier : 다른 항목과 구분하는데 필요한 정보

#### URL, URN 이란?
- URL - Locator : 리소스가 있는 위치를 지정
- URN - Name : 리소스에 이름을 부여
- 위치를 변할 수 있지만, 이름은 변하지 않는다. 
-  **앞으로 강의에서는 URI를 URL과 같은 의미로 이야기한다.**

#### URL 전체 문법
-  `scheme://[userinfo@]host[:port][/path][?query][#fragment]`
- `https://www.google.com:443/search?q=hello&hl=ko`
- 위에 있는 URL 주소를 바탕으로 아래의 URL을 설명한다. 
> - 프로토콜(https)
> - 호스트명 `www.google.com`
> - 포트 번호 (443)
> - 패스 (/search)
> - 쿼리 파라미터 (q=hello&hl=ko)


### 1. URL scheme (**`https`**)
- 주로 프로토콜을 사용한다. 
- 프로토콜 : 어떤 방식으로 자원에 접근할 것인가에 대한 약속 규칙이다. 
    - 예) http, https, ftp 등등
- http는 80포트, https 는 443 포트를 주로 사용한다. 포트는 생략 가능하다. 
- https 는 http에 보안을 추가한 것이다. (HTTP Secure)

### 2. URL userinfo  (**`[userinfo@]`**)
- URL에 사용자 정보를 포함해서 인증한다. 
- 거의 사용하지 않는다. 

### 3. URL host  (**`www.google.com`**)
- 호스트명
- 도메인 명 또는 IP주소를 직접 사용 가능하다.  

### 4. URL port  (**`443`**)
- 접속 포트를 말하며, 일반적으로 생략한다. 생략 시 http는 80포트, https 는 443포트이다. 

### 5. URL path  (**`search`**)
- 리소스 경로(path), 계층적 구조
- 예)
    - /home/file1.jpg
    - /members
    - /members/100, /items/iphone12

### 6. URL query  (**`?q=hello&hl=ko`**)
 - key=value 형태
 - ?로 시작, &로 추가 가능하다. ?keyA=valueA&keyB=valueB
 - query parameter, query string 등으로 불린다 .
 - 웹 서버에 제공하는 파라미터, 문자 형태이다. 

### 7. URL fragment
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/727736cf-216c-405a-92f4-350219ba4632)

- html 내부 북마크 등에 사용한다.
- 서버에 전송하는 정보가 아니다.

***

## 웹 브라우저 요청 흐름
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/728b87e1-bea2-429e-8454-fb4cd65af09b)
> - DNS 를 조회하여 IP주소 알아내고, 포트번호를 사용하여 HTTP 요청 메시지를 생성한다. 
- HTTP 요청 메시지
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/695a1fb7-7d96-41e2-bc31-2a800e2e3528)

- 패킷을 생성할 때, 전송 메시지에 HTTP 메시지를 넣어 서버에 전달한다.  
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/730dc612-5973-44b8-9315-b8f81ba8efa9)

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/108902ba-7251-453e-a84d-f2c0f5a900ac)
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/f3939b5d-6f4e-467f-b37d-ccacdea49d42)
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/27b80739-d607-4bc2-8a6c-551c0e000688)
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/ae0d87a4-3a41-4951-9686-f9543d42872a)

> - TCP/IP 패킷을 만들어 클라이언트에서 요청 패킷을 서버에 전달하면, HTTP 응답 메시지를 넣은 패킷을 만들어 서버에서 응답 패킷을 전달한다. 
> - 응답 패킷이 클라이언트에 도착하면 웹 브라우저 HTML 렌더링을 하여 원하는 정보를 얻을 수 있다. 

***
## 🔗출처

- 김영한 - [모든 개발자를 위한 HTTP 웹 기본 지식](https://www.inflearn.com/course/http-웹-네트워크/dashboard)

***
