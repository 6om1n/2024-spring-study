# ⭐ 6. 스프링 MVC - 기본 기본 기능
# ✏️ 프로젝트 생성
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/87d86bc4-75f6-418e-803f-569a8b2cda8a)

- 프로젝트 생성시 주의할 점 : War 가 아닌 **Jar** 를 선택하기
- **Jar**를 사용하면 항상 내장 서버 (톰켓) 을 사용하고, `webapp` 경로도 사용하지 않는다. 즉, 내장 서버 사용에 최적화 되어 있는 기능이다. 
- **War** 를 사용하면 내장 서버도 사용 가능하지만, 주로 외부 서버에 배포하는 목적으로 사용한다. 

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/f4ed0da3-375b-4b6a-8a3a-adf5f699df5e)

- `Annotation Processors` 에 가서 `enable annotaion processing`  체크하기 !

## 🔍 Welcome 페이지 만들기
- 스프링 부트에 `Jar` 를 사용하면 `/resources/static/` 위치에 `index/html` 파일을 두면 Welcome 페이지로 처리해준다. 
- 스프링 부트가 지원하는 정적 컨텐츠 위치 `/index.html` 이 있으면 된다 !

``` html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<ul>
    <li>로그 출력
        <ul>
            <li><a href="/log-test">로그 테스트</a></li>
        </ul>
    </li>
    <!-- -->
    <li>요청 매핑
        <ul>
            <li><a href="/hello-basic">hello-basic</a></li>
            <li><a href="/mapping-get-v1">HTTP 메서드 매핑</a></li>
            <li><a href="/mapping-get-v2">HTTP 메서드 매핑 축약</a></li>
            <li><a href="/mapping/userA">경로 변수</a></li>
            <li><a href="/mapping/users/userA/orders/100">경로 변수 다중</a></li>
            <li><a href="/mapping-param?mode=debug">특정 파라미터 조건 매핑</a></li>
            <li><a href="/mapping-header">특정 헤더 조건 매핑(POST MAN 필요)</a></li>
            <li><a  href="/mapping-consume">미디어 타입 조건 매핑 Content-Type(POSTMAN 필요)</a></li>
            <li><a href="/mapping-produce">미디어 타입 조건 매핑 Accept(POST MAN 필요)</a></li>
        </ul>
    </li>
    <li>요청 매핑 - API 예시
        <ul>
            <li>POST MAN 필요</li>
        </ul>
    </li>
    <li>HTTP 요청 기본
        <ul>
            <li><a href="/headers">기본, 헤더 조회</a></li>
        </ul>
    </li>
    <li>HTTP 요청 파라미터
        <ul>
            <li><a href="/request-param-v1?username=hello&age=20">요청 파라미터 v1</a></li>
            <li><a  href="/request-param-v2?username=hello&age=20">요청 파라미터 v2</a></li>
            <li><a href="/request-param-v3?username=hello&age=20">요청 파라미터 v3</a></li>
            <li><a href="/request-param-v4?username=hello&age=20">요청 파라미터 v4</a></li>
            <li><a href="/request-param-required?username=hello&age=20">요청 파라미터 필수</a></li>
            <li><a href="/request-param-default?username=hello&age=20">요청 파라미터 기본 값</a></li>
            <li><a href="/request-param-map?username=hello&age=20">요청 파라미터 MAP</a></li>
            <li><a href="/model-attribute-v1?username=hello&age=20">요청 파라미터
                @ModelAttribute v1</a></li>
            <li><a href="/model-attribute-v2?username=hello&age=20">요청 파라미터
                @ModelAttribute v2</a></li>
        </ul>
    </li>
    <li>HTTP 요청 메시지
        <ul>
            <li>POST MAN</li>
        </ul>
    </li>
    <li>HTTP 응답 - 정적 리소스, 뷰 템플릿
        <ul>
            <li><a href="/basic/hello-form.html">정적 리소스</a></li>
            <li><a href="/response-view-v1">뷰 템플릿 v1</a></li>
            <li><a href="/response-view-v2">뷰 템플릿 v2</a></li>
        </ul>
    </li>
    <li>HTTP 응답 - HTTP API, 메시지 바디에 직접 입력
        <ul>
            <li><a href="/response-body-string-v1">HTTP API String v1</a></li>
            <li><a href="/response-body-string-v2">HTTP API String v2</a></li>
            <li><a href="/response-body-string-v3">HTTP API String v3</a></li>
            <li><a href="/response-body-json-v1">HTTP API Json v1</a></li>
            <li><a href="/response-body-json-v2">HTTP API Json v2</a></li>
        </ul>
    </li>
</ul>
</body>
</html>
```
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/f166045d-269c-408e-b432-31b272e3e9f9)

***
# ✏️ 로깅 간단히 알아보기
### ⭐  운영 시스템에서는 `System.out.println()` 같은 시스템 콘솔을 사용해서 정보를 출력하지 않고, 로깅 라이브러리를 사용해서 **로그** 를 출력한다. 

> 스프링 부트 로깅 라이브러리는 기본으로 다음 로깅 라이브러리를 사용한다. 
> - SLF4J - http://www.slf4j.org
> - Logback - http://logback.qos.ch
> - 로그 라이브러리는 많이 있는데 그것을 통합해서 인터페이스로 제공하는 것이 SLF4J 라이브러리 이다. 

- 로그 선언
> - `private Logger log = LoggerFactory.getLogger(getClass());`
> - `private static final Logger log = LoggerFactory.getLogger(Xxx.class)`
> - `@Slf4j` : 롬복 사용 가능

- 로그 호출
> - `log.info("hello")`
> - `System.out.println("hello")`

- `LogTestController`
``` java
package hello.springmvc.basic;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LogTestController {

    // private final Logger log = LoggerFactory.getLogger(getClass()); // 안에 클래스 정보만 있으면 된다.

    @RequestMapping ("/log-test")
    public String logTest() {
        String name = "Spring";
        System.out.println("name = " + name); // 이건 무조건 출력이 되서 사용하면 안된다.

        log.trace("trace my log=" + name); // 이렇게 하면 쓸모없는 리소스가 사용이 되어서 이렇게 하면 안된다.
        log.trace("trace log={}", name); // 두개는 기록되지 않음
        log.debug("debug log={}", name); // 두개는 기록되지 않음
        log.info ("info log={}", name);
        log.warn(" warn log={}", name);
        log.error("error log={}", name);
        return "ok";
    }
}
```

- 매핑 정보
> - `@RestController`
> - `@Controller` 는 반환 값이 `String` 이면 뷰 이름으로 인식된다.  따라서 **뷰를 찾고 뷰가 랜더링** 된다. 
> - `@RestController` 는 반환 값으로 뷰를 찾는 것이 아니라, **HTTP 메시지 바디에 바로 입력** 한다.  따라서 실행 결과로 ok 메시지를 받을 수 있다. 

- 위의 코드를 실행시켰을 때 볼 수 있는 로그
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/7bdda8dd-efd5-4d32-9ff5-1c406c44f83e)

> - 시간, 로그 레벨, 프로세스 ID, 쓰레드 명, 클래스 명, 로그 메시지가 로깅되는 것을 확인할 수 있다.
> - 위와 같은 실행 결과를 내기 위해 `application.properties` 에서  
`logging.level.hello.springmvc=trace` 를 추가하였다. 

- 로그 레벨 설정 변경하여 출력 결과 확인하기
> - LEVEL : `TRACE > DEBUG > INFO > WARN > ERROR`
> - 개발 서버는 debug 출력
> - 운영 서버는 info 출력

## ⭐ 올바른 로그 사용법
- `log.debug("data="+data)`
    - 로그 출력 레벨을 info로 설정해도 해당 코드에 있는 "data="+data 가 실제 실행이 된다. (실제 로그는 출력되지 않음)
    - 결과적으로 문자 더하기 연산이 발생한다.  (자원 낭비)
- `log.debug("data={}", data)`
    - 로그 출력 레벨을 info 로 설정하면 아무 일이 발생하지 않는다. 
    - 앞과 같은 의미 없는 연산이 발생하지 않는다. 

- 로그 사용시 장점
    - 쓰레드 정보, 클래스 이름 같은 부가 정보를 함께 볼 수 있고, 출력 모양을 조정할 수 있다. 
    - 로그 레벨에 따라 개발 서버에서는 모든 로그를 출력하고, 운영서버에서는 출력하지 않는 등 로그를 상황에 맞게 조절할 수 있다. 
    - 시스템 아웃 콘솔에만 출력하는 것이 아니라, 파일이나 네트워크 등, 로그를 별도의 위치에 남길 수 있다. 특히 파일로 남길 때는 일별, 특정 용량에 따라 로그를 분할하는 것도 가능하다. 
    - 성능도 일반 System.out 보다 좋다. 
    - 따라서 실무에서는 꼭 로그를 사용해야 한다. 
  
***

# ✏️ 요청 매핑
- ` MappingController`
``` java
package hello.springmvc.basic.requestmapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class MappingController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @RequestMapping("/hello-basic")
    public String helloBasic() {
        log.info("helloBasic");
        return "OK";
    }
}
```
### 🔍  HTTP 메서드
- `@RequestMapping` 에 `method` 속성으로 HTTP 메서드를 지정하지 않으면 HTTP 메서드와 무관하게 호출된다. 
- GET, HEAD, POST, PUT, PATCH, DELETE 모두 허용

### 🔍 HTTP 메서드 매핑
``` java
  /**
     * method 특정 HTTP 메서드 요청만 허용
     * GET, HEAD, POST, PUT, PATCH, DELETE
     */
    @RequestMapping(value = "/mapping-get-v1", method = RequestMethod.GET)
    public String mappingGetV1() {
        log.info("mappingGetV1");
        return "ok";
    }
```
> - 위의 코드는 GET 메서드만을 사용해야 한다. 만약 POST 요청을 하면 HTTP 405 상태코드 (Method Not Allowed) 를 반환한다. 

### 🔍 HTTP 메서드 매핑 축약
``` java
/**
     * 편리한 축약 애노테이션 (코드보기)
     * @GetMapping
     * @PostMapping
     * @PutMapping
     * @DeleteMapping
     * @PatchMapping
     */
    @GetMapping(value = "/mapping-get-v2")
    public String mappingGetV2() {
        log.info("mapping-get-v2");
        return "ok";
    }
```
> - `method = ` 이런 식으로 사용하지 않고 HTTP 메서드를 축약한 애노테이션을 사용하는 것이 직관적이다. 

### 🔍 PathVariable(경로 변수) 사용
``` java

    /**
     * PathVariable 사용
     * 변수명이 같으면 생략 가능
     * @PathVariable("userId") String userId -> @PathVariable String userId
     */
    @GetMapping("/mapping/{userId}")
    public String mappingPath(@PathVariable("userId") String data) {
        log.info("mappingPath userId={}", data);
        return "ok";
    }
```
- 실행 : http://localhost:8080/mapping/userA
- `@RequestMapping` 은 URL 경로를 템플릿화 할 수 있는데, `@PathVariable` 을 사용하면 매칭 되는 부분을 편리하게 조회할 수 있다. 
- `@PathVariable` 의 이름과 파라미터 이름이 같으면 생략할 수 있다. 

### 🔍 PathVariable 사용 - 다중
``` java
/**
     * PathVariable 사용 다중
     */
    @GetMapping("/mapping/users/{userId}/orders/{orderId}")
    public String mappingPath(@PathVariable String userId, @PathVariable Long
            orderId) {
        log.info("mappingPath userId={}, orderId={}", userId, orderId);
        return "ok";
    }
```

### 🔍 특정 파라미터 조건 매핑
``` java
 /**
     * 파라미터로 추가 매핑
     * params="mode",
     * params="!mode"
     * params="mode=debug"
     * params="mode!=debug" (! = )
     * params = {"mode=debug","data=good"}
     */
    // 특정 파라미터 정보가 있으면 출력이 된다라는 뜻 -> 잘은 사용안한다.
    @GetMapping(value = "/mapping-param", params = "mode=debug")
    public String mappingParam() {
        log.info("mappingParam");
        return "ok";
    }
```
- 잘은 사용되지 않는다. 

### 🔍 특정 헤더 조건 매핑
``` java
 /**
     * 특정 헤더로 추가 매핑
     * headers="mode",
     * headers="!mode"
     * headers="mode=debug"
     * headers="mode!=debug" (! = )
     */
    @GetMapping(value = "/mapping-header", headers = "mode=debug")
    public String mappingHeader() {
        log.info("mappingHeader");
        return "ok";
    }
```
- Postman 으로 테스트 해야 한다. 

### 🔍 미디어 타입 조건 매핑 - HTTP 요청 Content-Type
``` java
 /**
     * Content-Type 헤더 기반 추가 매핑 Media Type
     * consumes="application/json"
     * consumes="!application/json"
     * consumes="application/*"
     * consumes="*\/*"
     * MediaType.APPLICATION_JSON_VALUE
     */
    @PostMapping(value = "/mapping-consume", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String mappingConsumes() {
        log.info("mappingConsumes");
        return "ok";
    }
```
> - HTTP 요청의 Content-Type 헤더를 기반으로 미디어 타입으로 매핑한다. 
만약 맞지 않으면 HTTP 415 상태코드를 반환한다. 
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/66b2e3cd-441e-4c84-b972-4c02eec36243)

### 🔍 미디어 타입 조건 매핑 - HTTP 요청 Accept, produce
``` java
 /**
     * Accept 헤더 기반 Media Type
     * produces = "text/html"
     * produces = "!text/html"
     * produces = "text/*"
     * produces = "*\/*"
     */
    @PostMapping(value = "/mapping-produce", produces = MediaType.TEXT_HTML_VALUE)
    public String mappingProduces() {
        log.info("mappingProduces");
        return "ok";
    }
```
> - HTTP 요청 Accept 헤더를 기반으로 미디어 타입으로 매핑한다. 
> - 만약 맞지 않으면 HTTP 406 상태코드(Not Acceptable) 을 반환한다.
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/b13fe697-7c2a-4e87-9c60-3463905df2ff)
 
***

# ✏️ 요청 매핑 - API 예시
### 🔍 회원 관리 API
- 회원 목록 조회 : GET    `/users`
- 회원 등록 : POST `/users`
- 회원 조회 : GET `/users/{userId}`
- 회원 수정 : PATCH `/users/{userId}`
- 회원 삭제 : DELETE `/users/{userId}`

- `MappingClassController`
``` java
package hello.springmvc.basic.requestmapping;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mapping/users")
public class MappingClassController {
    /**
     * 회원 목록 조회 : GET   /users
     * 회원 등록 : POST      /users
     * 회원 조회 : GET       /users/{userId}
     * 회원 수정 : PATCH     /users/{userId}
     * 회원 삭제 : DELETE    /users/{userId}
     */
    @GetMapping
    public String user() {
        return "get users";
    }

    @PostMapping
    public String addUser() {
        return "post user";
    }

    @GetMapping("/{userId}")
    public String findUser(@PathVariable String userId) {
        return "get userId=" + userId;
    }
    @PatchMapping("/{userId}")
    public String updateUser(@PathVariable String userId) {
        return "update userId=" + userId;
    }
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable String userId) {
        return "delete userId=" + userId;
    }
}
```

- `/mapping` 은 다른 예제들과 구별하기 위해 사용함. 
- `@RequestMapping("/mapping/users")`
    - 클래스 레벨에 매핑 정보를 두면 메서드 레벨에서 해당 정보를 조합해서 사용한다. 
    - 위의 예시에서는 우선 string으로 반환값만 지정해주었다. 

### Postman 으로 테스트
- 회원 목록 조회 : GET   `/mapping/users`
- 회원 등록 : POST   `/mapping/users`
- 회원 조회 : GET   `/mapping/users/id1`
- 회원 수정 :PATCH   `/mapping/users/id1`
- 회원 삭제 : DELETE    `/mapping/users/id1`

- url은 같고 method를 사용하여 구분한다. 
***
# ✏️ HTTP 요청 - 기본, 헤더 조회

- RequestHeaderController
``` java
package hello.springmvc.basic.request;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;

@Slf4j
@RestController
public class RequestHeaderController {

    @RequestMapping("/headers")
    public String headers(HttpServletRequest request,
                          HttpServletResponse response,
                          HttpMethod httpMethod,
                          Locale locale,
                          @RequestHeader MultiValueMap<String, String> headerMap,
                          @RequestHeader("host") String host,
                          @CookieValue(value="myCookie", required = false) String cookie) {

        log.info("request={}", request);
        log.info("response={}", response);
        log.info("httpMethod={}", httpMethod);
        log.info("locale={}", locale);
        log.info("headerMap={}", headerMap);
        log.info("header host={}", host);
        log.info("myCookie={}", cookie);
        return "ok";
    }
}
```
> - `HttpMethod` : HTTP 메서드를 조회한다. `org.springframework.http.HttpMethod`
> - `Locale` : Locale 정보를 조회한다. 
> - `@RequestHeader MultiValueMap<String, String> headerMap`
>     - 모든 HTTP 헤더를 MultiValueMap 형식으로 조회한다. 
> - `@RequestHeader("host") String host`
>     - 특정 HTTP 헤더를 조회한다. 
>     - 속성 : 필수 값 여부 - `required` , 기본 값 속성 - `defaultValue`
> - `@CookieValue(value = "MyCookie", required = false) String cookie`
>     - 특정 쿠키를 조회한다. 
>     - 속성 : 필수 값 여부 - `required` , 기본 값 - `defaultValue`
> - `MultiValueMap`
>     - Map 과 유사한데, 하나의 키에 여러 값을 받을 수 있다.
>     - HTTP headerm HTTP 쿼리 파라미터와 같이 하나의 키에 여러 값을 받을 때 사용한다. 
>     - keyA=vaule&keyA=value2
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/0de82100-b396-48ef-b6ba-7138b56600ac)

- `@Slf4j` 는 아래와 같은 코드를 자동으로 생성해서 로그를 선언해준다.
- 개발자는 편리하게 `log` 라고 사용하면 된다. 
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/1f33a1bd-6996-44d4-9306-589d30f75e22)
***

# ✏️  HTTP 요청 파라미터 - 쿼리 파라미터, HTML Form
- HTTP 요청 메시지를 통해 클라이언트에서 서버로 데이터를 전달하는 방법 알아보기
### ⭐ 클라이언트에서 서버로 요청 데이터를 전달할 때는 주로 다음 3가지 방법을 사용한다. 
- **GET** - 쿼리 파라미터
> - /url?username=hello&age=20
> - 메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달
> - 예) 검색, 필터, 페이징 등에서 많이 사용하는 방식

- **POST** - HTML Form
> - content-type: application/x-www-form-urlencoded
> - 메시지 바디에 쿼리 파라미터 형식으로 전달 username=hello&age=20
> - 예) 회원 가입, 상품 주문, HTML Form 사용

- **HTTP message body** 에 데이터를 직접 담아서 요청
> - HTTP API에서 주로 사용, JSON, XML, TEXT
> - 데이터 형식은 주로 JSON 사용
> - POST, PUT, PATCH

### 🔍 요청 파라미터 - 쿼리 파라미터, HTML Form
- `HttpServletRequest` 의 `request.getParameter()` 를 사용ㅇ하면 다음 두 가지 요청 파라미터를 조회할 수 있다.
- GET, 쿼리 파라미터 전송
> -  예시 ) `content-type: application/x-www-form-urlencoded`

- POST, HTML Form 전송
> 
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/87d3d3db-6c22-4ecb-8acd-9daee0465eef)

- GET 쿼리 파라미터 전송 방식이든, POST Html Form 전송 방식이든 둘다 형식이 같으므로 구분없이 조회할 수 있다. 
- 이것을 간단히 **요청 파라미터(request parameter) 조회**라 한다. 

- `RequestParamController`
``` java
 package hello.springmvc.basic.request;


import hello.springmvc.basic.HelloData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Slf4j
@Controller
public class RequestParamController {

    @RequestMapping("/request-param-v1")
    public void requestParamV1(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));
        log.info("username={}, age={}", username, age);

        response.getWriter().write("ok");

    }
}
```

> - 반환 타입이 없으면서 이렇게 응답에 값을 직접 넣으면, view로 조회 되지 않는다.
> - 실행 : `http://localhost:8080/request-param-v1?username=hello&age=20`

- Post Form 페이지 생성
- 리소스는 `/resources/static` 아래에 두면 스프링 부트가 자동으로 인식한다. 
- `main/resources/static/basic/hello-form.html`
``` html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<form action="/request-param-v1" method="post">
    username: <input type="text" name="username" />
    age: <input type="text" name="age" />
    <button type="submit">전송</button>
</form>
</body>
</html>
```
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/899755fa-dcaf-40d4-9fc4-f9d2a16b2ce3)

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/58953efe-7b7f-43f6-a4dc-87dd78804476)

***

# ✏️  HTTP 요청 파라미터 - `@RequestParam`
- 스프링이 제공하는 `@RequestParam` 을 사용하면 요청 파라미터를 매우 편리하게 사용할 수 있다.
### 🔍 `requestparamV2`
``` java
 @ResponseBody
    @RequestMapping("request-param-v2")
    public String requestParamV2 (
        @RequestParam("username") String memberName,
        @RequestParam("age") int memberAge) {

        log.info("username={}, age={}", memberName, memberAge);
        return "ok";
    }
```
> - 원래는 `@Controller` 이면서 String이면 뷰리졸버로 "ok" 라는 뷰를 찾게된다. 
> - `@RequestParam` : 파라미터 이름으로 바인딩
> - `@ResponseBody` : View 조회를 무시하고, HTTP message body 에 직접 해당 내용 입력 -> `@RestController` 와 같은 의미이다. 
> - **`@RequestParam`** 의 `name(value)` 속성이 파라미터 이름으로 사용
> - `@RequestParam("username") String memberName`
 > - request.getParameter("username")

### 🔍 `requestparamV3`
``` java
  @ResponseBody
    @RequestMapping("request-param-v3")
    public String requestParamV3 (
            @RequestParam String username,
            @RequestParam int age) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }
```
> - HTTP 파라미터 이름이 변수 이름과 같으면 `@RequestParam(name="xx")` 생략 가능

### 🔍 `requestparamV4`
``` java
 @ResponseBody
    @RequestMapping("request-param-v4")
    public String requestParamV4 (String username, int age) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }
```
> - `@RequestParam` 사용
> - String, int 등의 단순 타입이면 `@RequestParam` 도 생략 가능하다. 
> - 이와 같이 다 없애서 사용할 수 있지만, 요청 파라미터의 이름과 맞아야한다. 
> - `@RequestParam` 애노테이션을 생략하면 `required=false` 를 적용한다. 

### 🔍 파라미터 필수 여부 - `requestparamRequired`
``` java
 @ResponseBody
    @RequestMapping("request-param-required")
    public String requestParamRequired(
            @RequestParam(required = true) String username,
            @RequestParam(required = false) int age) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }
```
> - `@RequestParam.required`: 파라미터 필수 여부 
>     - 기본 값이 파라미터 필수 (`true`) 이다. 
> - 그냥 기본적으로 `/request-param-required` 요청하면, 필수 값인 username 이 없으므로 400 예외가 발생한다. 
>- `/request-param-required?username=` 인 경우 파라미터 이름만 있고 값이 없는 경우는 빈문자로 통과된다. 
> - 만약 코드를 `@RequestParam(required=false) int age` 로 수정했을 경우
> - 위의 경우 파라미터에 값이 없는 경우에는 null을 int 기본형에 입력하는 것이 불가능하다. (500 예외 발생)
> - 따라서 null 을 받을 수 있는 Integer로 변경하거나, `defaultValue` 를 사용해야한다. 

### 🔍 기본 값 적용 - `requestparamDefault`
``` java
    @ResponseBody
    @RequestMapping("request-param-default")
    public String requestParamDefalut(
            @RequestParam(required = true, defaultValue = "guest") String username,
            @RequestParam(required = false, defaultValue = "-1") int age) {

        log.info("username={}, age={}", username, age);
        return "ok";
    }
```

>- 파라미터에 값이 없는 경우 `defaultValue` 를 사용하면 기본 값을 적용할 수 있다. 
> - 이미 기본 값이 있기 때문에 `required` 는 의미가 없다. 
> - 빈 문자인 경우에도, 값을 입력해주지 않은 경우에도 설정한 기본 값이 적용된다. 

### 🔍 파라미터를 Map 으로 조회하기 - `requestparamMap`
``` java
   @ResponseBody
    @RequestMapping("request-param-map")
    public String requestParamMap(
           @RequestParam Map<String, Object> paramMap) {
        log.info("username={}, age={}", paramMap.get("username"), paramMap.get("age"));
        return "ok";
    }
```
> - 파라미터를 Map, MultiValueMap 으로 조회할 수 있다.
> - `@RequestParam Map`, 
>     - `Map(key=value)`
> - `@RequestParam MultiValueMap`
>     - MultiValueMap(key=[value1, value2, ...] ex) (key=userIds, value=[id1, id2])`
> - 파라미터 값이 1개가 확실하다면, `Map` 을 사용해도 되지만, 그렇지 않다면 `MultiValueMap` 을 사용하자. 
> - 하지만, 보통 파라미터 값은 1개를 넣는다. 

***

# ✏️ HTTP 요청 파라미터 - `@ModelAttribute`
- 실제 개발을 하다보면, 요청 파라미터를 받아서 필요한 객체를 만들고, 그 객체에 값을 넣어주어야 한다. 
``` java
@RequestParam String username;
@RequestParam int age;

HelloData data = new HelloData();
data.setUsername(username);
data.setAge(age);
```

- 스프링은 이 과정을 완전히 자동화해주는 `@ModelAttribute`  기능을 제공한다. 
- 먼저 요청 파라미터를 바인딩 받을 객체를 만들어보자
-  `HelloData`
``` java
package hello.springmvc.basic;

import lombok.Data;

@Data
public class HelloData {
    private String username;
    private int age;
}
```

> - 롬복 `@Data`
> - `@Getter` , `@Setter` , `@ToString`, `@EqualsAndHashCode` , `@RequiredArgsConstructor` 를 자동으로 적용해준다. 

### 🔍 `@ModelAttribute` 적용 - modelAttributeV1
``` java
    @ResponseBody
    @RequestMapping("/model-attribute-v1")
    public String modelAttributeV1(@ModelAttribute HelloData helloData) {
        log.info("username={}, age= {}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }
```

> - 위와 같이 코드를 작성했더니, `HelloData` 객체가 생성되고, 요청 파라미터의 값도 모두 들어가 있다. 
> - 스프링 MVC는 `@ModelAttribute` 가 있으면 다음을 실행한다. 
>      - `HelloData` 객체를 생성한다. 
>      - 요청 파라미터의 이름으로 `HelloData` 객체의 프로퍼티를 찾는다. 그리고, 해당 프로퍼티의 setter를 호출해서 파라미터의 값을 입력(바인딩)한다. 
>      - 예) 파라미터 이름이 `username` 이면 `setUsername()` 메서드를 찾아서 호출하면서 값을 입력한다. 

- 프로퍼티
- 객체에 `getUsername()`, `setUsername()` 메서드가 있으면, 이 객체는 `username` 이라는 프로퍼티를 가지고 있다. 
- `username` 프로퍼티의 값을 변경하면 `setUsername()` 이 호출되고, 조회하면 `getUsername()` 이 호출된다. 
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/2d063279-4654-48e3-a996-f6be702e6e6c)

### 🔍 `@ModelAttribute` 생략 - modelAttributeV2
``` java
    @ResponseBody
    @RequestMapping("/model-attribute-v2")
    public String modelAttributeV2(HelloData helloData) {
        log.info("username={}, age= {}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }
```

> - `@ModelAttribute` 는 생략할 수 있다. 그러나, `@RequestParam` 도 생략할 수 있으니 혼란이 발생할 수 있다. 
> - 스프링은 해당 생략 시 다음과 같은 규칙을 적용한다. 
> - `String` , `int`, `Integer` 같은 단순 타입 = `@RequestParam`
> - 나머지 = `@ModelAttribute`

***

# ✏️ HTTP 요청 메시지 - 단순 텍스트
- 요청 파라미터와 다르게, HTTP 메시지 바디를 통해 데이터가 직접 넘어오는 경우는 `@RequestParam` , `@ModelAttribute` 를 사용할 수 없다. 
- HTTP 메시지 바디의 데이터를 `InputStream` 을 사용해서 직접 읽을 수 있다. 
### 🔍 `RequestBodyStringController`
``` java
@Slf4j
@Controller
public class RequestBodyStringController {

    @PostMapping("/request-body-string-v1")
    public void requestBodyString(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);

        response.getWriter().write("ok");
    }
}
```
>- Postman 을 사용해서 테스트를 할 때
> - POST `http://localhost:8080/request-body-string-v1`
> - Body -> row, text 선택
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/283f7dba-2138-4481-a2cd-f4ff34d94f92)
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/58c48659-296a-4e50-9e19-3b8174e056f2)

### 🔍 Input, Output 스트림, Reader - requestBodyStringV2
``` java
 @PostMapping("/request-body-string-v2")
    public void requestBodyStringV2(InputStream inputStream, Writer responseWriter) throws IOException {

        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
        log.info("messageBody={}", messageBody);
        responseWriter.write("ok");
    }
```

> - InputStream(Reader) : HTTP 요청 메시지 바디의 내용을 직접 조회
> - OutputStream(Writer) : HTTP 응답 메시지의 바디에 직접 결과 출력

### 🔍 HttpEntity - requestBodyStringV3
``` java
    @PostMapping("/request-body-string-v3")
    public HttpEntity<String> requestBodyStringV3(HttpEntity<String> httpEntity) throws IOException {

        String messageBody = httpEntity.getBody();
        log.info("messageBody={}", messageBody);
        return new HttpEntity<String>("ok");
    }
```
> - **HttpEntity** : HTTP header, body 정보를 편리하게 조회
>     - 메시지 바디 정보를 직접 조회
>     - 요청 파라미터를 조회하는 기능과 관계 없음 `@RequestParam` X, `@ModelAttribute` X
> - HttpEntity 응답에도 사용 가능 
>     - 메시지 바디 정보 직접 반환
>     - 헤더 정보 포함 기능
>     - view 조회x
> - `HttpEntity` 를 상속 받은 다음 객체들도 같은 기능을 제공한다. 
> - **RequestEntity**
>     - HttpMethod, url 정보가 추가, 요청에서 사용
> - **ResponseEntity**
>     - HTTP 상태 코드 설정 가능, 응답에서 사용
>     - `return new ResponseEntity<String>("Hello World", responseHeaders, 
 HttpStatus.CREATED)`

### 🔍 RequestBody - reqeustBodyStringV4
``` java
    @ResponseBody
    @PostMapping("/request-body-string-v4")
    public HttpEntity<String> requestBodyStringV4(@RequestBody String messageBody) {
        log.info("messageBody={}", messageBody);
        return new HttpEntity<String>("ok");
    }
```

> - `@RequestBody`
> - `@RequestBody` 를 사용하면 HTTP 메시지 바디 정보를 편리하게 조회할 수 있다. 참고로 헤더 정보가 필요하다면 `HttpEntity` 를 사용하거나 `@RequestHeader` 를 사용하면 된다. 
> - 이렇게 메시지 바디를 직접 조회하는 기능은 요청 파라미터를 조회하는 `@RequestParam` , `@ModelAttribute` 와는 전혀 관계가 없다. 

- 요청 파라미터 vs HTTP 메시지 바디
> - 요청 파라미터를 조회하는 기능 : `@RequestParam`, `@ModelAttribute`
> - HTTP 메시지 바디를 직접 조회하는 기능 : `@RequestBody`

- `@ResponseBody`
> - `@ResponseBody` 를 사용하면 응답 결과를 HTTP 메시지 바디에 직접 담아서 전달할 수 있다. 
> - 물론 이 경우에도 view 를 사용하지 않는다. 

***

# ✏️ HTTP 요청 메시지 - JSON
- HTTP API에서 주로 사용하는 JSON 데이터 형식을 조회해보자. 
### 🔍 RequestBodyJsonController
``` java
package hello.springmvc.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.springmvc.basic.HelloData;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Controller
public class RequestBodyJsonController {

    private ObjectMapper objectMapper = new ObjectMapper();

    @PostMapping("/request-body-json-v1")
    public void requestBodyJsonV1 (HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        log.info("messageBody={}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        response.getWriter().write("ok");
    }
}
```
> - HttpServletRequest 를 사용해서 직접 HTTP 메시지 바디에서 데이터를 읽어와서, 문자로 변환한다. 
> - 문자로 된 JSON 데이터를 Jackson 라이브러리인 `objectMapper` 를 사용해서 자바 객체로 변환한다. 
> - Postman 으로 테스트
> - POST `http://localhost:8080/request-body-json-v1`
> - raw, JSON, content-type : application/json
> - `{"username":"hello" , "age":20}`

### 🔍 requestBodyJsonV2 - `@RequestBody`문자 변환
``` java
 @ResponseBody
    @PostMapping("/request-body-json-v2")
    public String requestBodyJsonV2 (@RequestBody String messageBody) throws IOException {

        log.info("messageBody={}", messageBody);
        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());

        return "ok";
    }
```
> - 이전에 학습했던 `@RequestBody` 를 사용해서 HTTP 메시지에서 데이터를 꺼내고 messageBody 에 저장한다. 
> - 문자로 된 JSON 데이터인 `messageBody` 를 `objectMapper` 를 통해서 자바 객체로 변환한다. 

### 🔍 requestBodyJsonV3 - `@RequestBody` 객체 변환
- V2 에서 문자로 변환하고 다시 json으로 변환하는 과정이 불편해서 `@ModelAttribute` 처럼 한 번에 객체로 변환하기 
``` java
 @ResponseBody
    @PostMapping("/request-body-json-v3")
    public String requestBodyJsonV3(@RequestBody HelloData helloData) {
        log.info("username={}, age={}", helloData.getUsername(), helloData.getAge());
        return "ok";
    }
```
> - `@RequestBody` 객체 파라미터
>     - `@RequestBody HelloData data`
>     - `@RequestBody` 에 직접 만든 객체를 지정할 수 있다.
> - `HttpEntity`, `@RequestBody` 를 사용하면 HTTP 메시지 컨버터가 HTTP 메시지 바디의 내용을 우리가 원하는 문자나 객체 등으로 변환해준다. 
> - HTTP 메시지 컨버터는 문자 뿐만 아니라 JSON 도 객체로 변환해주는데, 우리가 방금 V2 에서 했던 작업을 대신 처리해준다. 
> - `@RequestBody` 는 생략 불가능

### 🔍 requestBodyJsonV4 - HttpEntity
``` java
  @ResponseBody
    @PostMapping("/request-body-json-v4")
    public String requestBodyJsonV4(HttpEntity<HelloData> httpEntity) {
        HelloData data = httpEntity.getBody();
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return "ok";
    }
```

### 🔍 requestBodyJsonV45 
``` java
    @ResponseBody
    @PostMapping("/request-body-json-v5")
    public HelloData requestBodyJsonV5(@RequestBody HelloData data) {
        log.info("username={}, age={}", data.getUsername(), data.getAge());
        return data;
    }
```
> - `@ResponseBody`
> - 응답의 경우에도 `@ResponseBody` 를 사용하면 해당 객체를 HTTP 메시지 바디에 직접 넣어줄 수 있다. 이 경우에도 `HttpEntity` 를 사용해도 된다. 
> - `@RequestBody` 요청
>     - JSON 요청 -> HTTP 메시지 컨버터 -> 객체
> - `@ResponseBody` 응답
>     - 객체 -> HTTP 메시지 컨버터 -> JSON 응답
***
# ✏️ 응답 - 정적 리소스, 뷰 템플릿
- 스프링(서버)에서 응답 데이터를 만드는 방법은 크게 3가지이다. 
> - 정적 리소스
>     - 예) 웹 브라우저에 정적인 HTML, css, js를 제공할 때는 **정적 리소스** 를 사용한다. 
> - 뷰 템플릿 사용
>     - 예) 웹 브라우저에 동적인 HTML을 제공할 때는 뷰 템플릿을 사용한다. 
> - HTTP 메시지 사용
>     - HTTP API를 제공하는 경우에는 HTML이 아니라 데이터를 전달해야 하므로, HTTP 메시지 바디에 JSON 같은 형식으로 데이터를 실어 보낸다. 

### 🔍 정적 리소스
- 스프링 부트는 클래스패스의 다음 디렉토리에 있는 정적 리소스를 제공한다. 
> - `/static` , `/public` , `/resources` , `/META-INF/resources`

- `src/main/resources/static`
> - 위의 경로는 리소스를 보관하는 곳이고, 클래스패스의 시작 경로이다. 
> - 따라서 다음 디렉토리에 리소스를 넣어두면 스프링 부트가 정적 리소스로 서비스를 제공한다. 

- **정적 리소스 경로**
> - `src/main/resources/static`
> - `src/main/resources/static/basic/hello-form.html` 이 경로에 파일이 들어있으면
> - `http://localhost:8080/basic/hello-from.html` 웹 브라우저에서 이렇게 실행하면 된다. 

### 🔍 뷰 템플릿
> - 뷰 템플릿을 거쳐서 HTML이 생성되고, 뷰가 응답을 만들어서 전달한다. 
> - 일반적으로 HTML을 동적으로 생성하는 용도로 사용하지만, 다른 것들도 가능하다. 
> - 스프링 부트는 `src/main/resources/templates` 기본 뷰 템플릿 경로를 제공한다. 
 - 뷰 템플릿 생성 `src/main/resources/templates/response/hello.html`
``` html
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<p th:text="${data}">empty</p>
</body>
</html>
```

- ResponseViewController - 뷰 템플릿을 호출하는 컨트롤러
``` java
package hello.springmvc.basic.response;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ResponseViewController {

    @RequestMapping("/response-view-v1")
    public ModelAndView responseViewV1() {
        ModelAndView mav = new ModelAndView("response/hello")
                .addObject("data", "hello!");

        return mav;
    }

    @RequestMapping("/response-view-v2")
    public String responseViewV2(Model model) {
        model.addAttribute("data", "hello!");
        return "response/hello";
    }
    
    @RequestMapping("/response/hello")
    public void responseViewV3(Model model) {
        model.addAttribute("data", "hello!");
    }
}
```
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/8245a4c8-cd35-4434-8ac5-f52649bfd185)
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/57aee242-0215-4b91-a949-38daa0a541f8)

- String을 반환하는 경우 - View or HTTP 메시지
> - `@ResponseBody` 가 없으면 `response/hello` 로 뷰 리졸버가 실행되어 뷰를 찾고, 렌더링 한다. 
> - `@ResponseBody` 가 있으면 뷰 리졸버를 실행하지 않고, HTTP 메시지 바디에 직접 `response/hello` 라는 문자가 입력된다. 
> - 이 상황에서는 뷰의 논리 이름인 `response/hello` 를 반환하면 `templates/response/hello.html` 경로의 뷰 템플릿이 렌더링 되는 것을 확인할 수 있다. 

- Void 를 반환하는 경우 
> - `@Controller` 를 사용하고, `HttpServletResponse` , `OutputStream(Writer)` 같은 HTTP 메시지 바디를 처리하는 파라미터가 없으면 요청 URL 을 참고해서 뷰 이름으로 사용한다. 
>     - 요청 URL : `/response/hello`
>     - 실행 : `templates/response/hello.html`
> - **이 방식은 명시성이 너무 떨어지고, 딱 맞는 경우도 많이 없어서 권장하지 않는다.**

- HTTP 메시지
> - `@ResponseBody` , `HttpEntity` 를 사용하면, 뷰 템플릿을 사용하는 것이 아니라, HTTP 메시지 바디에 직접 응답 데이터를 출력할 수 있다. 

***

# ✏️  HTTP 응답 - HTTP API, 메시지 바디에 직접 입력 
- HTTP API 를 제공하는 경우에는 HTML이 아니라 데이터를 전달해야 하므로, HTTP 메시지 바디에 JSON 같은 형식으로 데이터를 실어 보낸다. 
> - 참고) HTML이나 뷰 템플릿을 사용해도 HTTP 응답 메시지 바디에 HTML 데이터가 담겨서 전달된다. 
> - 여기서 설명하는 내용은 정적 리소스나 뷰 템플릿을 거치지 않고, 직접 HTTP 응답 메시지를 전달하는 경우를 말한다. 

- ResponseBodyController
``` java
package hello.springmvc.basic.response;

import hello.springmvc.basic.HelloData;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Slf4j
@RestController
public class ResponseBodyController {

    @GetMapping("response-body-string-v1")
    public void responseBodyV1(HttpServletResponse response) throws IOException {
        response.getWriter().write("ok");
    }
    @GetMapping("response-body-string-v2")
    public ResponseEntity<String> responseBodyV2() {
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }

    //@ResponseBody
    @GetMapping("/response-body-string-v3")
    public String responseBodyV3() {
        return "ok";
    }

    @GetMapping("/response-body-json-v1")
    public ResponseEntity<HelloData> responseBodyJsonV1() {
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);
        return new ResponseEntity<>(helloData, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    //@ResponseBody
    @GetMapping("/response-body-json-v2")
    public HelloData responseBodyJsonV2() {
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);
        return helloData;
    }
}
```

### 🔍 responseBodyV1
- 서블릿을 직접 다를 때처럼 HttpServletResponse 객체를 통해서 HTTP 메시지 바디에 직접 `ok` 응답 메시지를 전달한다. 
- `response.getWriter().write("ok")`

### 🔍 responseBodyV2
- `ResponseEntity` 엔티티는 `HttpEntity` 를 상속 받았는데, HttpEntity 는 HTTP 메시지의 헤더, 바디 정보를 가지고 있다. 
- `ResponseEntity` 는 여기에 더해서 HTTP 응답 코드를 설정할 수 있다. 
- `HttpStatus.CREATED` 로 변경하면 201 응답이 나가는 것을 확인할 수 있다. 

### 🔍 responseBodyV3
- `@ResponseBody` 를 사용하면 view를 사용하지 않고, HTTP 메시지 컨버터를 통해서 HTTP 메시지를 직접 입력할 수 있다.
- `ResponseEntity` 도 동일한 방식으로 동작한다. 

### 🔍 responseBodyJsonV1
- `ResponseEntity` 를 반환한다.  HTTP 메시지 컨버터를 통해서 JSON 형식으로 변환되어서 반환된다. 

### 🔍 responseBodyJsonV2
- `ResponseEntity` 는 HTTP 응답 코드를 설정할 수 있는데, `@ResponseBody` 를 사용하면 이런 것을 설정하기 까다롭다. 
- `@ResponseStatus(HttpStatus.OK)` 애노테이션을 사용하면 응답 코드도 설정할 수 있다. 
- 애노테이션이기 때문에 응답 코드를 동적으로 변경할 수는 없다. 프로그램 조건에 따라서 동적으로 변경하려면 `ResponseEntity` 를 사용하면 된다. 

### `@RestController`
- `@Controller` 대신에 `@RestController` 애노테이션을 사용하면 해당 컨트롤러에 `@ResponseBody` 가 적용되는 효과가 있다. 
- 뷰 템플릿을 사용하는 것이 아니라, HTTP 메시지 바디에 직접 데이터를 입력한다. 이름 그대로 Rest API(HTTP API)를 만들 때 사용하는 컨트롤러이다. 

***

# ✏️  HTTP 메시지 컨버터
> - 뷰 템플릿으로 HTML을 생성해서 응답하는 것이 아니라, HTTP API처럼 JSON 데이터를 HTTP 메시지 바디에서 직접 읽거나 쓰는 경우 HTTP 메시지 컨버터를 사용하면 편리하다. 

- `@ResponseBody` 사용 원리
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/e4a0e194-f087-464d-8cd4-4ac03af05df2)
> - `@ResponseBody` 를 사용
>     - HTTP의 BODY에 문자 내용을 직접 반환
>     - `viewResolver` 대신에 `HttpMessageConverter` 가 동작
>     - 기본 문자 처리 : `StringHttpMessageConverter`
>     - 기본 객체 처리 : `MappingJaskson2HttpMessageConverter`
>     - byte 처리 등등 기타 여러 HttpMessageConverter 가 기본으로 등록되어있음

- 스프링 MVC는 다음의 경우에 HTTP 메시지 컨버터를 적용한다. 
- HTTP 요청 : `@RequestBody` , `HttpEntity(RequestEntity)`
- HTTP 응답 : `@ResponseBody` , `HttpEntity(ResponseEntity)`

- `org.springframework.http.converter.HttpMessageConverter`
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/953cb75a-4b0b-42d5-a9e8-b329c18d868f)

> - HTTP 메시지 컨버터는 HTTP 요청, HTTP 응답 둘 다 사용된다. 
> - `canRead()` , `canWrite()` : 메시지 컨버터가 해당 클래스, 미디어타입을 지원하는지 체크
> - `read()` , `write()` : 메시지 컨버터를 통해서 메시지를 읽고 쓰는 기능

- 스프링 부트 기본 메시지 컨버터
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/5445aa85-ada7-4fb1-9e36-489c86f4a842)

> - 스프링 부트는 다양한 메시지 컨버터를 제공하는데, 대상 클래스 타입과 미디어 타입 둘을 체크해서 사용여부를 결정한다. 
> - 만약 만족하지 않으면 다음 메시지 컨버터로 우선 순위가 넘어간다. 

- `ByteArrayHttpMessageConverter` : `byte[]` 데이터를 처리한다. 
> - 클래스 타입 : `byte[]` , 미디어 타입 `*/*`
> - 요청 예) `@RequestBody byte[] data`
> - 응답 예) `@ResponseBody return byte[]` 쓰기 미디어 타입 `application/octet-stream`

- `StringHttpMessageConverter` : `String` 문자로 데이터를 처리한다. 
> - 클래스 타입 : `String` , 미디어타입 : `*/*`
> - 요청 예) `@RequestBody String data`
> - 응답 예) `@ResponseBody return ok` 쓰기 미디어 타입 `text/plain`

- `MappingJackson2HttpMessageConverter` : application/json
> - 클래스 타입 : `HashMap` , 미디어타입 : `application/json`
> - 요청 예) `@RequestBody HelloData data`
> - 응답 예) `@ResponseBody return helloData` 쓰기 미디어 타입 `application/json`

### 예시
- StringHttpMessageConverter
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/90d2c4f3-35e7-4729-a2cb-5070fba251e4)

- MappingJackson2HttpMessageConverter
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/81a2dd3a-b957-452c-bdf3-c7a014e005da)

- ?
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/80adb54d-919a-42ec-b6df-f2f36c95d4b9)

### 🔍 Http 요청 데이터 읽기
- HTTP 요청이 오고, 컨트롤러에서 `@RequestBody`, `HttpEntity` 파라미터를 사용한다. 
- 메시지 컨버터가 메시지를 읽을 수 있는지 확인하기 위해 `canRead()` 를 호출한다. 
> - 대상 클래스 타입을 지원하는가
>     - 예) `@RequestBody` 의 대상 클래스 (`byte[]`, `String`, `HelloData`)
> - HTTP 요청의 Content-Type 미디어 타입을 지원하는가
>     - 예) `text/plain` , `application/json` , `*/*`
> - `canRead()` 조건을 만족하면 `read()` 를 호출해서 객체 생성하고, 반환한다. 

### 🔍 Http 응답 데이터 생성
- 컨트롤러에서 `@ResponseBody` , `HttpEntity` 로 값이 반환된다.
- 메시지 컨버터가 메시지를 쓸 수 있는지 확인하기 위해 `canWrite()` 를 호출한다. 
> - 대상 클래스 타입을 지원하는가. 
>     - 예) return 의 대상 클래스 (`byte[]`, `String`, `HelloData`)
> - HTTP 요청의 Accept 미디어 타입을 지원하는가 (정확히는 `@RequestMapping` 의 `produces`)
>     - 예) `text/plain` , `application/json` , `*/*`
- `canWrite()` 조건을 만족하면 `write()` 를 호출해서 HTTP 응답 메시지 바디에 데이터를 생성한다. 
 
***

# ✏️ 요청 매핑 핸들러 어댑터 구조
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/160d8aa5-cf66-4c97-bca3-e6e5b06e3df3)

> - 위의 그림에서는 HTTP 메시지 컨버터는 스프링 MVC 어디쯤에서 사용되는지 보이지 않는다. 
> - 모든 비밀은 애노테이션 기반의 컨트롤러, `@RequestMapping` 을 처리하는 핸들러 어댑터인 `RequestMappingHandlerAdapter` 에 있다. 

- RequestMappingHanlderAdapter 동작 방식
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/3f34b621-ee96-48fd-b7df-19f5aba28de1)

- ArgumentResolver
> - 애노테이션 기반의 컨트롤러는 매우 다양한 파라미터를 사용할 수 있었다. 
> - `HttpServletRequest`, `Model` 은 물론이고, `@RequestParam` , `@ModelAttribute` 같은 애노테이션과 `@RequestBody` , `HttpEntity` 같은 HTTP 메시지를 처리하는 부분까지 매우 큰 유연함을 보여주었다. 
> - 이렇게 파라미터를 유연하게 처리할 수 있는 이유가 바로 `ArgumentResolver` 덕분이다. 
> - 애노테이션 기반 컨트롤러를 처리하는 `RequestMappingHandlerAdapter` 는 `ArgumentResolver` 를 호출해서 컨트롤러가 필요로 하는 다양한 파라미터의 값(객체)을 생성한다. 그리고 이렇게 파라미터의 값이 모두 준비되면 컨트롤러를 호출하면서 값을 넘겨준다. 
> - 스프링은 30개가 넘은 `ArgumentResolver` 를 기본으로 제공한다. 
> - https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-arguments 에서 확인가능

- 정확히 `HandlerMethodArgumentResolver` 인데 줄여서 `ArgumentResolver` 라고 부른다. 
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/0d0a6394-1e50-4f28-891a-363c270a5301)

- 동작방식
> - `ArgumentResolver` 의 `supportsParameter()` 를 호출해서 해당 파라미터를 지원하는지 체크하고, 지원하면 `resolverArgument()` 를 호출해서 실제 객체를 생성한다. 이렇게 생성된 객체가 컨트롤러 호출 시 넘어간다. 
> - 원한다면 이 인터페이스를 확장해서 원하는 `ArgumentResolver` 를 만들 수도 있다.

- ReturnValueHandler
> - `HandlerMethodReturnValueHandler` 를 줄여서 `ReturnValueHandler` 라고 부른다. 
> - `ArgumentResolver` 와 비슷한데, 응답 값을 변환하고 처리한다. 
> - 스프링은 10여개가 넘는 `ReturnValueHandler` 를 지원한다.
> - 예) `ModelAndView` , `@ResponseBody`, `HttpEntity` , `String`
> -  https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-ann-return-types 여기서 응답 값 목록을 확인할 수 있다. 

### 🔍  HTTP 메시지 컨버터
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/b8cc4366-1615-4f98-ac68-757e37a1788b)

> - HTTP 메시지 컨버터를 사용하는 `@RequestBody` 도 컨트롤러가 필요로 하는 파라미터의 값에 사용된다. 
> - `@ResponseBody` 의 경우도 컨트롤러의 반환 값을 이용한다. 

- 요청의 경우
> - `@RequestBody` 를 처리하는 `ArgumentResolver` 가 있고, `HttpEntity` 를 처리하는 `ArgumentResolver` 가 있다. 
> - `ArgumentResolver` 들이 HTTP 메시지 컨버터를 사용해서 필요한 객체를 생성한다. 

- 응답의 경우
> - `@ResponseBody` 와 `HttpEntity` 를 처리하는 `ReturnValueHandler` 가 있다. 여기에서 HTTP 메시지 컨버터를 호출해서 응답 결과를 만든다. 

> - 스프링 MVC는 `@RequestBody` , `@ResponseBody` 가 있으면 `RequestResponseBodyMethodProcessor(ArgumentResolver, ReturnValueHandler 둘 다 구현)` , `HttpEntity` 가 있으면 `HttpEntityMethodProcessor(ArgumentResolver, ReturnValueHandler) 둘 다 구현` 을 사용한다. 

### 🔍 확장
- 스프링은 다음을 모두 인터페이스로 제공한다. 따라서 필요하면 언제든지 기능을 확장할 수 있다. 
- `HandlerMethodArgumentResolver`
- `HandlerMethodReturnValueHandler`
- `HttpMessageConverter`

> - 스프링이 필요한 대부분의 기능을 제공하기에 실제 기능을 확장할 일이 많지 않다. 
> - 기능 확장은 `WebMvcConfigurer` 를 상속 받아서 스프링 빈으로 등록하면 된다. 

- WebMvcConfigurer 확장
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/8b28ac2a-25e9-4313-b99e-df28efbd17a0)

***
# 🔗출처
- 김영한 - [스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1/dashboard)
