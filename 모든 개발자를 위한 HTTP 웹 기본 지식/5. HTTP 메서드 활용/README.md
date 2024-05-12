# :star: 5. HTTP 메서드 활용 
## 클라이언트에서 서버로 데이터 전송
### 클라이언트에서 서버로 데이터를 전달하는 방식은 크게 2가지이다. 
- 쿼리 파라미터를 통한 데이터 전송
    - GET
    - 주로 정렬 필터 (검색어)
- 메시지 바디를 통한 데이터 전송
    - POST, PUT, PATCH
    - 회원 가입, 상품 주문, 리소스 등록, 리소스 변경

### 클라이언트에서 서버로 데이터가 전송되는 4가지 상황이 있다. 
- 정적 데이터 조회
    - 이미지, 정적 텍스트 문서
- 동적 데이터 조회
    - 주로 검색, 게시판 목록에서 정렬 필터 (검색어)
- HTML Form을 통한 데이터 전송
    - 회원 가입, 상품 주문, 데이터 변경
- HTTP API 를 통한 데이터 전송
    - 회원 가입, 상품 주문, 데이터 변경
    - 서버 to 서버, 앱 클라이언트, 웹 클라이언트

#### :heavy_check_mark: 정적 데이터 조회
- 쿼리 파라미터 미사용
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/7ded740a-2bbf-4ba3-8961-7718967538ce)

> - 쿼리 파라미터를 사용하지 않고 GET 을 사용하여 정적 데이터를 전달한다. 
> - 정적 데이터 조회는 GET을 사용하고, 일반적으로 쿼리 파라미터 없이 리소스 경로로 단순하게 조회 가능하다. 

#### :heavy_check_mark: 동적 데이터 조회
- 쿼리 파라미터 사용
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/5f200a41-7466-45c8-b278-211a1ea70c8a)

> - 쿼리 파라미터를 기반으로 정렬 필터해서 결과를 동적으로 생성한다. 
> - 주로 검색, 게시판 목록에서 사용한다. 
> - 조회 조건을 줄여주는 필터, 조회 결과를 정렬하는 정렬 조건에 주로 사용한다. 
> - 조회는 GET을 사용한다. GET은 쿼리 파라미터를 사용해서 데이터를 전달한다.  

#### :heavy_check_mark: HTML  Form 데이터 전송
- POST 전송 - 저장
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/3f01b1f5-343a-49e0-87a1-f9fd43ac4347)

> - 위와 같이 Form 에 username 이랑 age 를 입력하고 POST를 사용해서 전달 할 경우, 해당 정보가 서버로 전송된다. 

- GET 전송 - 저장
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/9d4fad25-60e7-4ad1-acac-45f1c4cadf52)

> - 위의 사진의 경우에는 GET은 조회로만 사용해야 하는데, save를 사용하여 리소스 변경이 발생하는 곳에는 사용하면 안된다. 

- GET 전송 - 조회
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/89e1640c-abd3-4d39-84a1-6dcfa7d3b316)

> - get을 사용하여 username과 age를 조회하는 용도로 사용할 수 있다. 

- multipart/form-data
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/d6da5dba-e0a9-4d66-a25d-d1090cfb56f0)

> - `enctype="multipart/form-data"` 를 사용할 경우 파트 별로 나누어 username, age, file 등등의 정보들이 구분되서 들어가는 것을 확인할 수 있다. 

### :hatching_chick: HTML Form  데이터 전송 정리
- HTML Form submit시 POST 전송
    - 예 ) 회원 가입, 상품 주문, 데이터 변경
- Content-Type : application/x-www-form-urlencoded 사용
    - form의 내용을 메시지 바디를 통해서 전송 (key=value, 쿼리 파라미터 형식)
    - 전송 데이터를 url encoding 처리
- HTML Form은 GET 전송도 가능하다. 
- Content-Type : multipart/form-data
    - 파일 업로드 같은 바이너리 데이터 전송 시 사용
    - 다른 종류의 여러 파일과 폼의 내용을 함께 전송 가능하다. 
-  :pushpin: HTML Form 전송은 GET, POST만 지원한다.       

#### :heavy_check_mark: HTML  API 데이터 전송
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/77d0b01a-405c-4ffd-a5b6-5b4d3f2d0691)

> - 안드로이드 아이폰 애플리케이션에서 클라이언트에서 서버로 데이터를 바로 전송해야하느 ㄴ경우를 HTTP API로 데이터를 전송한다고 표현한다. 
> - json 데이터를 넣고 Content-Type : application/json 이라고 하고 데이터를 넘겨주면 된다. 


### :hatching_chick: HTML API  데이터 전송 정리
- 서버 to 서버
    - 백엔드 시스템 통신
- 앱 클라이언트
    - 아이폰 , 안드로이드
- 웹 클라이언트
    - HTML에서 Form 전송 대신 자바 스크립트를 통한 통신에 사용한다.  (AJAX)
    - 예 ) React, VueJs 같은 웹 클라이언트와 API 통신한다. 
- POST, PUT, PATCH : 메시지 바디를 통해 데이터를 전송한다. 
- GET : 조회, 쿼리 파라미터로 데이터 전달한다. 
- Content-Type : application/json을 주로 사용한다. (사실상 표준이다. )
    - TEXT, XML, JSON 등등
    -  
***

## HTTP API 설계 예시
- HTTP API - 컬렉션
    - POST 기반 등록
    -  예 ) 회원 관리 API  제공
- HTTP API - 스토어
    -  PUT 기반 등록
    - 예 ) 정적 컨텐츠 관리, 원격 파일 관리
- HTML Form 사용
    - 웹 페이지 회원 관리
    - GET, POST만 지원     

### 회원 관리 시스템 - POST - 신규 자원 등록 특징
- 클라이언트는 등록될 리소스의 URI를 모른다.
    - 회원 등록 /members -> POST
    - POST /members
- 서버가 새로 등록된 리소스 URI를 생성해준다.
    - HTTP/1.1 201 Created
    - Location : /members/100
- 컬렉션
    - 서버가 관리하는 리소스 디렉토리
    - 서버가 리소스의 URI 를 생성하고 관리
    - 여기서 컬렉션은 /members

### 파일 관리 시스템 -PUT - 신규 자원 등록 특징
- 클라이언트가 리소스 URI를 알고 있어야 한다. 
    - 파일 등록 /files/{filename} -> PUT
    - PUT /files/star.jpg
- 클라이언트가 직접 리소스의 URI를 지정한다. 
- 스토어 (Store)
    - 클라이언트가 관리하는 리소스 저장소
    - 클라이언트가 리소스의 URI를 알고 관리
    - 여기서 스토어는 /files
### HTML Form 사용
- HTML FORM은 GET, POST만 지원한다. 
- AJAX 같은 기술을 사용해서 해결 가능하다. -> 회원 API 참고
- 여기서는 순수 HTML, HTML FORM 이야기이다. 
- GET, POST만 지원하므로 제약이 있다. 
- **컨트롤 URI**
    - GET, POST만 지원하므로 제약이 있다. 
    - 이런 제약을 해결하기 위해 동사로 된 리소스 경로를 사용한다. 
    - POST의 /new, /edit, /delete 가 컨트롤 URI 이다. 
    - HTTP 메서드로 해결하기 애매한 경우 사용한다. (HTTP API 포함)

### :hatching_chick: 정리
- HTTP API - 컬렉션
    - POST 기반 등록
    - 서버가 리소스 URI 결정
- HTTP API - 스토어
    - PUT 기반 등록
    - 클라이언트가 리소스 URI 결정
- HTML FORM 사용
    - 순수 HTML + HTML form 사용
    - GET, POST만 지원   
      
***

## 🔗출처

- 김영한 - [모든 개발자를 위한 HTTP 웹 기본 지식](https://www.inflearn.com/course/http-웹-네트워크/dashboard)
