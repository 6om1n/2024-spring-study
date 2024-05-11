# ⭐ 4. HTTP 메서드
## HTTP API를 만들어보자

- API URI를 설계할 때 이런식으로 하는 것이 좋은 방법일까 ? 
> 결론부터 이야기하자면 아니다 !

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/07f5c9ed-4210-45b6-863d-ecbeddae7fbc)

### :star: 가장 중요한 것은 **리소스 식별** 이다. 

- 리소스의 의미란?
    - 회원을 등록하고 수정하고 조회하는 것이 리소스가 아니다. 
    - **회원이라는 개념 자체가 바로 리소스이다.**
-  리소스를 식별하는 방법
    - 회원을 등록하고 수정하고 조회하는 것을 모두 배제
    - **회원이라는 리소스만 식별하면 된다. :arrow_right: 회원 리소스를 URI에 매핑한다.**
    
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/4021cdaf-83fd-4785-bcf5-dd65edb7fbf9)

- 위의 사진과 같이 리소스만을 가지고 구분하다보면 URI를 설계할 때 문제가 발생한다. 
- **:star:  따라서 리소스와 행위를 분리해야한다.** 
    - 리소스 : 회원
    - 행위 : 조회, 등록, 삭제, 변경
- **URI는 리소스만을 식별해야하고, 리소스만 식별하면 HTTP 메소드가 조회, 삭제 이런 역할을 대신해준다.** 
   
***

## HTTP 메서드 - GET, POST

### 주요 HTTP 메서드의 종류
- GET :  리소스 조회
- POST : 요청 데이터 처리, 주로 등록에 사용
- PUT : 리소스를 대체, 해당 리소스가 없으면 생성
- PATCH : 리소스 부분 변경
- DELETE : 리소스 삭제

##### 기타 메서드
- HEAD : GET과 동일하지만 메시지 부분을 제외하고, 상태 줄과 헤더만 반환
- OPTIONS : 대상 리소스에 대한 통신 가능 옵션을 설명 (주로 CORS에서 사용)
- CONNECT : 대상 리소스로 식별되는 서버에 대한 터널을 설정
- TRACE : 대상 리소스에 대한 경로를 따라 메시지 루프백 테스트를 수행
> - CONNECT와 TRACE는 많이 사용하지 않는다. 

### GET
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/ea359e7d-813f-4857-8e23-ffbb59da724e)

- GET은 리소스를 조회하고, 서버에 전달하고 싶은 데이터는 query(쿼리 파라미터, 쿼리 스트링)를 통해서 전달한다. 
- 메시지 바디를 사용해서 데이터를 전달할 수 있지만, 지원하지 않는 곳이 많아서 권장하지는 않는다. 

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/a3fd4cc8-5803-48a8-a755-9df3df14eab1)
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/702a084f-4d55-4935-b5aa-1b6b0f282cf7)

> GET 으로 서버에 메시지를 전달해서 서버에 도착하면 서버에서 응답 데이터를 보낸다.

### POST
 -  요청 데이터를 처리한다.
 - **메시지 바디를 통해서 서버로 요청 데이터를 전달**하면, 서버는 요청데이터를 **처리한다.**
     - 메시지 바디를 통해 들어온 데이터를 처리하는 모든 기능을 수행한다. 
- 주로 전달된 데이터로 신규 리소스 등록, 프로세스 처리에 사용한다. 

- 리소스 등록 - 메시지 전달
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/c37e691a-2f28-49e8-a499-838ae37633e9)
- 리소스 등록 - 신규 리소스 생성
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/c29cd9fa-e76e-4bb0-a503-7cc745962072)
- 리소스 등록 - 응답 데이터
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/4aabae8b-8f36-4069-9e8f-b986ba6d2f61)

- POST는 등록할 때 많이 쓰인다. 
- 신규로 자원이 생성되면 보통 HTTP 201 Created 라고 보내고, location에 자원이 생성된 경로를 보내준다. 

#### POST 는 다음과 같은 기능에 사용된다. 
- HTML 양식에 입력 된 필드와 같은 데이터 블록을 데이터 처리 프로세스에 제공한다.
    - ex ) HTML FORM에 입력한 정보로 회원 가입, 주문 등에서 사용
- 게시판, 뉴스 그룹, 메일링 리스트, 블로그 또는 유사한 기사 그룹에 메시지를 게시한다.
    - ex ) 게시판 글쓰기, 댓글 달기
- 서버가 아직 식별하지 않은 새 리소스 생성
    - ex ) 신규 주문 생성
- 기존 자원에 데이터 추가
    - ex ) 한 문서 끝에 내용 추가하기

#### 정리 : 이 리소스 URI에 POST 요청이 오면 요청 데이터를 어떻게 처리할 지 리소스마다 따로 정해야한다. :arrow_right: 정해진 것이 없다.         

### POST 정리
1. 새 리소스 생성(등록)
- 서버가 아직 식별하지 않은 새 리소스 생성
2. 요청 데이터 처리
- 단순한 데이터를 생성하거나, 변경하는 것을 넘어서 프로세스를 처리해야 하는 경우
- ex ) 주문에서 결제완료 -> 배달시작 -> 배달완료처럼 단순히 값 변경을 넘어 프로세스의 상태가 변경되는 경우
- POST의 결과로 새로운 리소스가 생성되지 않을 수도 있음
3. 다른 메서드로 처리하기 애매한 경우
- ex )  JSON으로 조회 데이터를 넘겨야 하는데, GET 메서드를 사용하기 어려운 경우
- 애매하면 보통 POST를 많이 사용한다. 
> JSON으로 조회용으로 메시지 바디에 넣고 싶은데 GET 메서드는 메시지 바디를 허용하지 않는 서버들이 많기 때문에 조회이지만 POST를 사용하기도 ### 한다. 

***

##  HTTP 메서드 - PUT, PATCH, DELETE
### PUT
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/159ca714-4e47-4773-9c58-85fe15924989)

- 리소스를 대체한다. 
    - 리소스가 있으면 대체하고, 없으면 생성한다. 
    - 쉽게 말해서 덮어버린다. 
- :star: 중요 ! 클라이언트가 리소스를 식별한다. 
    - 클라이언트가 리소스 위치를 알고  URI를 지정한다. 이것이 POST와의 차이점이다.
 

- 리소스가 있는 경우 전달한 내용으로 완전히 교체한다.     
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/d634aab7-82f0-4e0f-b9e9-cb1ce7c08f2a)
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/f8594e5f-a1c3-4534-9527-e471927ca1d4)

- 리소스가 없는 경우네는 신규 리소스를 생성한다. 
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/3266eaa8-cd93-4782-9c51-2ffb148d6487)
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/c317dc2b-809b-4fc1-98f7-69de9a7a9730)


- 주의 ! 리소스를 완전히 대체한다. 
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/d1a3a55e-a33c-4449-a8a2-72604dbc1a74)
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/4b89611a-a923-4c22-bd44-a7853e56f504)

> 이미 members/100 에는 "username" 과 "age" 가 있는 상태에서 put을 사용하여 "age" : 50 이라는 정보를 넘겨주면 age의 값만 바뀌는 것이 아니라 리소스가 "age" : 50 으로 완전히 대체된다. 

- 일부분 변경을 하고 싶을 때는 PATCH를 사용한다. 

### PATCH
- PATCH는 리소스를 부분 변경한다. 
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/dc46c011-a963-4d3e-ab2b-7f059439f7ae)
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/abbdfd52-d1f2-449b-869d-be41627302dd)

> 위의 사진과 같이 리소스를 부분적으로 변경하고 싶을 때는 PATCH를 사용한다. 
> 위의 예에서 "age": 50 을 patch 로 넘겨주면 해당 값만 수정되는 것을 확인할 수 있다. 

### DELETE
- DELETE는 리소스를 제거한다. 
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/4efe2fa2-0d72-41f8-8955-215ffdb90449)
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/9499867f-a273-45cd-a5d1-fbc6b41b1fbb)

> DELETE를 사용하여 전달할 경우 리소스가 제거되는 것을 확인할 수 있다. 

***

## HTTP 메서드의 속성
- 안전 (Sage Methods)
- 멱등 (Idempotent Methods)
- 캐시가능 (Cacheable Methods)

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/d90b7ea6-24a5-487e-aeae-b49d0a2a3a41)

#### 안전
- 호출해도 리소스를 변경하지 않는다. 
- 호출해서 로그 같은 것이 쌓여서 장애가 발생하는 것은 고려하지 않는다. 
- 안전은 해당 리소스만을 고려하며, 그런 부분은 고려하지 않는다. 
- 여러 번이나 한 번 호출했을 때 변경이 일어나지 않는 것은 안전하지 않다고 한다. 
- 즉, POST, PUT, DELETE 는 안전하지 않다. 

#### 멱등
- 한 번 호출하든 두 번 호출하든 100번 호출하든 결과가 똑같다는 것을 의미한다. 
- 멱등 메서드
    - GET : 한 번 조회하든, 두 번 조회하든 같은 결과가 조회된다. 
    - PUT : 결과를 대체한다. 따라서 같은 요청을 여러 번 해도 최종 결과는 같다. 
    - DELETE : 결과를 삭제한다. 같은 요청을 여러 번해도 삭제된 결과는 똑같다. 
    - :x: POST :  멱등이 아니다! 두 번 호출하면 같은 결제가 중복해서 발생할 수 있다.  
***
## 🔗출처

- 김영한 - [모든 개발자를 위한 HTTP 웹 기본 지식](https://www.inflearn.com/course/http-웹-네트워크/dashboard)

***

