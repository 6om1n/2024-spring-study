# :star: 7. HTTP 헤더 1 - 일반 헤더
## HTTP 헤더 개요
### HTTP 헤더의 용도
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/8a59fc79-408d-4cd5-9ec3-e2e7cfe260ca)

- HTTP 전송에 필요한 모든 부가정보
- 예) 메시지 바디의 내용, 메시지 바디의 크기, 압축, 인증, 요청 클라이언트, 서버 정보, 캐시 관리 정보 .. 의 내용이 들어있다. 

### HTTP 헤더 분류 - RFC2616(과거)
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/4462ff70-b82e-44b7-ad4c-87f9942a74d5)

- 헤더 분류
   - General 헤더 : 메시지 전체에 적용되는 정보 예) Connection: close
   - Request 헤더 : 요청 정보
   - Response 헤더 : 응답 정보 예) Server: Apache
   - Entity 헤더: 엔티티 바디 정보 예) Content-Type: text/html, Content-Length:3423

### HTTP BODY message body - RFC2616(과거)
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/236304f4-6a99-455b-b508-e2f8462083d0)

- 메시지 본문(message body) 은 엔티티 본문 (entity body)을 전달하는데 사용한다.
- 엔티티 본문은 요청이나 응답에서 전달할 실제 데이터가 들어있다.
- **엔티티 헤더**는 **엔티티 본문**의 데이터를 해석할 수 있는 정보를 제공한다.
   - 데이터 유형 (html, json), 데이터 길이, 압축 정보 등등

### :exclamation: 지금은 RFC2616이 폐기되었고, RFC7230~7235가 등장하였다 .

### RFC723x 변화
- 엔티티(Entity) -> 표현 (Representation)
- Representation = representation Metadata + Representation Data
- 표현 = 표현 메타데이터 + 표현 데이터

### HTTP BODY message body - RFC7230(최신)
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/27369b83-739c-476e-8af4-4f4446b4d154)

- 메시지 본문을 통해 표현 데이터를 전달한다.
- 메시지 본문 = 페이로드 (payload)
- **표현**은 요청이나 응답에서 전달 할 실제 데이터
- **표현 헤더는 표현 데이터** 를 해석할 수 있는 정보를 제공한다.
   - 데이터 유형 (html, json), 데이터 길이, 압축 정보 등등  

***

## 표현
- Content-Type : 표현 데이터의 형식
- Content-Encoding : 표현 데이터의 압축 방식
- Content-Language : 표현 데이터의 자연 언어
- Content-Length : 표현 데이터의 길이
- 표현 헤더는 전송, 응답 둘 다 사용

### Content-Type - 표현 데이터의 형식 설명
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/60fc0eae-871b-422a-8705-5ab4b58ef3c1)

- 미디어 타입, 문자 인코딩
- 예 ) text/html; charset=utf-8
- application/json
- image/png

### Content-Encoding -표현 데이터 인코딩
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/454daa6a-0376-472b-a234-558c02722280)

- 표현 데이터를 압축하기 위해 사용한다. 
- 데이터를 전달하는 곳에서 압축 후 인코딩 헤더 추가한다. 
- 데이터를 읽는 쪽에서 인코딩 헤더의 정보로 압축을 해제한다.
- 예 ) gzip, deflate, identity

### Content-Language -표현 데이터의 자연 언어
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/20f08519-eb1f-4f74-bd31-9df43ce884d8)
- 예) ko, en, en-US

### Content-Length - 표현 데이터의 길이
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/bf1c8d1f-908f-4d73-adb4-9f23a87cf48b)
- 바이트 단위
- Transfer-Encoding (전송 코딩)을 사용하면 Content-Length를 사용하면 안된다. 

***

## 콘텐츠 협상

### 협상 (콘텐츠 네고시에이션) - 클라이언트가 선호하는 표현 요청
- Accept : 클라이언트가 선호하는 미디어 타입 전달
- Accept-Charset : 클라이언트가 선호하는 문자 인코딩
- Accept-Encoding : 클라이언트가 선호하는 압축 인코딩
- Accept-Language : 클라이언트가 선호하는 자연 연어 
- 협상 헤더는 요청 시에만 사용

#### :heavy_check_mark: Accpet-Language
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/60f5a4a1-d74c-4ed9-8a36-7a0b23fdadb5)
> - 다중 언어를 지원하는 서버에서 클라이언트가 Accept-Language : ko 로 보내면 해당하는 언어가 있으면 그 언어로 전달한다. 
> - 그러나, 해당 언어가 없을 때는 서버가 기본으로 지원하는 언어를 전달하기도 한다. 이 때, 우선순위를 정하여 원하는 언어를 받도록 할 수 있다. 

####  :heavy_check_mark:  협상과 우선순위 1 Quality Values(q)
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/ba1561eb-d1ae-4194-b055-f4a9101004df)
- Quality Values(q) 값 사용
- 0~1, **클수록 높은 우선순위**를 갖는다. 
- 생략하면 1을 나타낸다. 
- Accept-Language : ko-KR, ko;q=0.9, en-US;q=0.8, en;q=0.7
    -  1. ko-KR;q=1(q생략)
    -  2. ko;q=0.9
    -  3. en-US;q=0.8
    -  4. en;q=0.7

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/e125314a-2833-4712-a363-2fe7db6e9d0e)

> - 위의 사진과 같이 Quality Values 를 사용하여 원하는 언어의 우선순위를 설정하여 받을 수 있는 것을 확인할 수 있다.  

####  :heavy_check_mark:  협상과 우선순위 2 Quality Values(q)
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/28aea575-f42e-4aee-bd85-a67f4c5a73d4)

- 구체적인 것을 우선으로 한다. 
- Accept: text/*, text/plain, text/plain;format=flowed, */*
> 1.  text/plain;format=flowed
> 2. text/plain
> 3. text/*
> 4. */*

####  :heavy_check_mark:  협상과 우선순위 3 Quality Values(q)
- 구체적인 것을 기준으로 미디어 타입을 맞춘다. 
- Accept: text/*;q=0.3, text/html;q=0.7, text/html;level=1, text/html;level=2;q=0.4, */*;q=0.5
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/7119922a-b93f-4fbc-9d0e-0b3ed85b76f6)

***

## 전송 방식 
- 단순 전송
- 압축 전송
- 분할 전송
- 범위 전송

### :heavy_check_mark: 단순 전송 - Content-Length
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/1dc76dc5-be08-47ea-8c38-a4bf99161140)

### :heavy_check_mark: 압축 전송 - Content-Encoding
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/b0fa6c0b-cfbe-4d5c-98db-1731013b40c7)

> - 압축 방식에 따라서 클라이언트에게 압축 전송한다. 

### :heavy_check_mark: 분할 전송 - Transfer-Encoding
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/edfb3a94-47ec-4fe8-a1a2-60aa10c24105)
> - 클라이언트에게 내용을 분할로 전송한다. 이 때 내용이 나뉘어 전송이 되기 때문에 Content-Length는 사용하지 않는다. 

### :heavy_check_mark: 범위 전송 - Range, Content-Range
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/010dc86b-8d97-4823-b039-61cb48c92292)
> - 클라이언트에서 Range 를 지정하여 서버로 보내면 해당 범위에 해당하는 내용을 서버가 전달한다.  

***

## 일반 정보
- From : 유저 에이전트의 이메일 정보
- Referer : 이전 웹 페이지 주소
- User-Agent : 유저 에이전트 애플리케이션 정보
- Server : 요청을 처리하는 origin 서버의 소프트웨어 정보
- Date : 메시지가 생성된 날짜

 ### :heavy_check_mark: From - 유저 에이전트의 이메일 정보
- 일반적으로 잘 사용되지 않고, 검색 엔진 같은 곳에서 주로 사용한다. 
- 요청에서 사용한다. 

 ### :heavy_check_mark: Referer - 이전 웹 페이지 주소
- 현재 요청된 페이지의 이전 웹 페이지 주소
- A ->B 로 이동하는 경우 B를 요청할 때 Referer: A 를 포함해서 요청한다. 
- Referer 를 사용해서 유입 경로를 분석할 수 있다.
- 요청에서 사용을 하고, referer는 단어 referrer 의 오타인데 사용되고 있는 단어이다. 
 ### :heavy_check_mark: User-Agent - 유저 에이전트 애플리케이션 정보
- 클라이언트 애플리케이션 정보 (웹 브라우저 정보, 등등)
- 통계 정보
- 어떤 종류의 브라우저에서 장애가 발생하는 지 파악할 수 있다. 
- 요청에서 사용한다. 

 ### :heavy_check_mark: Server - 요청을 처리하는 ORIGIN 서버의 소프트웨어 정보
- Server : Apache/2.2.22 (Debian)
- server: nginx
- 응답에서 사용한다. 

 ### :heavy_check_mark: Date -메시지가 발생한 날짜와 시간
- 응답에서 사용한다. 

***

## 특별한 정보
- Host : 요청한 호스트 정보 (도메인)
- Location: 페이지 리다이렉션
- Allow : 허용 가능한 HTTP 메서드
- Retry-After : 유저 에이전트가 다음 요청을 하기까지 기다려야 하는 시간

 ### :heavy_check_mark: Host - 요청한 호스트 정보 (도메인)
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/0d928c43-3d27-41bf-b0e9-dc66b4665d5e)

> - 요청에서 사용하며, 필수이다.
> - 하나의 서버가 여러 도메인을 처리해야 할 때, 하나의 IP 주소에 여러 도메인이 적용되어 있을 때 필요하다. 

 ### :heavy_check_mark: Location - 페이지 리다이렉션
- 웹 브라우저는 3xx 응답의 결과에 Location 헤더가 있으면, Location 위치로 자동 이동한다. (리다이렉트)
- 201 (Created): Location 값은 요청에 의해 생성된 리소스 URI
- 3xx (Redirection) : Location 값은 요청을 자동으로 리다이렉션하기 위한 대상 리소스를 가리킨다.
 
 ### :heavy_check_mark: Allow - 허용 가능한 HTTP 메서드
- 405 (Method Not Allowed) 에서 응답에 포함해야한다. 
- Allow: GET, HEAD, PUT

 ### :heavy_check_mark: Retry-After - 유저 에이전트가 다음 요청을 하기까지 기다려야 하는 시간
- 503 (Service Unavailable) : 서비스가 언제까지 불능인지 알려줄 수 있다. 
- Retry-After: Fri, 31 Dec 1999 23:59:59 GMT (날짜 표기)
- Retry-After: 120 (초단위 표기)

***

## 인증
- 클라이언트 인증 정보를 서버에 전달한다. 
- WWW-Authenticate - 리소스 접근 시 필요한 인증 방법을 정의한다. 
    - 401 Unauthorized 응답과 함께 사용한다.  
***
## 쿠키
- Set-Cookie : 서버에서 클라이언트로 쿠키를 전달한다. (응답)
- Cookie : 클라이언트가 서버에서 받은 쿠키를 저장하고, HTTP 요청 시 서버로 전달한다. 

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/6f2bfe03-83e7-40bb-a787-114001f21bef)

> - 쿠키를 사용하지 않았을 경우, 로그인 후 welcome 페이지에 접근을 하여도 사용자를 인식하지 못하고 손님이라고만 응답한다. 그 이유는 Stateless 상태이기 때문이다.

### Stateless
- HTTP는 무상태 (Stateless) 프로토콜이다. 
- 클라이언트와 서버가 요청과 응답을 주고 받으면 연결이 끊어진다.
- 클라이언트가 다시 요청하면 서버는 이전 요청을 기억하지 못한다.
- 클라이언트와 서버는 서로 상태를 유지하지 않는다. 

> - 쿠키를 사용하지 않을 경우 모든 요청에 사용자 정보를 포함해서 보내는 방법이 있다.  ==> 비합리적이다. 
> - 따라서 쿠키를 사용한다. 

### 쿠키
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/f529e8c6-3561-4be3-b649-a30abd572451)

> - 로그인을 하면, 서버에서 해당 쿠키를 생성한다. 쿠키 저장소에 쿠키를 저장한다. 
> - 로그인 이후, 다시 페이지에 접근을 하면 쿠키 저장소에서 조회를 해와서 사용자가 홍길동이라는 정보를 알 수 있다. 
> - 모든 요청에 쿠키 정보를 자동으로 포함한다. 

- 쿠키 정보는 항상 서버에 전송된다. 
    - 네트워크  트래픽 추가 유발
    - 최소한의 정보만을 사용 (세선 id, 인증 토큰)
    - 서버에 전송하지 않고, 웹 브라우저 내부에 데이터를 저장하고 싶으면 웹 스토리지를 참고하자
    - :heavy_exclamation_mark: 주의 ! 보안에 민감한 데이터는 저장하면 안된다. (주민번호, 신용카드 번호 등등)

### 쿠키 - 생명주기 (Expires, max-age)
- Set-Cookie : expires=Sat, 26-Dec-2020 04:39:21 GMT
   - 만료일이 되면 쿠키 삭제
- Set-Cookie : max-age=3600
   - 0이나 음수를 지정하면 쿠키 삭제
- 세션 쿠키 : 만료 날짜를 생략하면 브라우저 종료 시 까지만 유지
- 영속 쿠키 : 만료 날짜를 입력하면 해당 날짜까지 유지

### 쿠키-도메인 (Domain)
 - 예) domain=example.org
- 명시: 명시한 문서 기준 도메인 + 서브 도메인 포함
-  domain=example.org를 지정해서 쿠키 생성
   - example.org는 물론이고
   - dev.example.org도 쿠키 접근
- 생략: 현재 문서 기준 도메인만 적용
   - example.org 에서 쿠키를 생성하고 domain 지정을 생략
   - example.org 에서만 쿠키 접근
   - dev.example.org는 쿠키 미접근

### 쿠키 - 경로 (Path)
- 예) path=/home
- 이 경로를 포함한 하위 경로 페이지만 쿠키 접근
- 일반적으로 path=/ 루트로 지정
- 예)
   - path=/home 지정
   - /home -> 가능
   - /home/level1 -> 가능
   - /home/level1/level2 -> 가능
   - /hello -> 불가능

### 쿠키 - 보안 (Secure, HttpOnly, SameSite)
- Secure
   - 쿠키는 http, https를 구분하지 않고 전송
   - Secure를 적용하면 https인 경우에만 전송
- HttpOnly
   - XSS 공격 방지
   - 자바스크립트에서 접근 불가(document.cookie)
   - HTTP 전송에만 사용
- SameSite
   - XSRF 공격 방지
   - 요청 도메인과 쿠키에 설정된 도메인이 같은 경우만 쿠키 
***

## 🔗출처

- 김영한 - [모든 개발자를 위한 HTTP 웹 기본 지식](https://www.inflearn.com/course/http-웹-네트워크/dashboard)
