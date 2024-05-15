# 2. :star:  서블릿

# :pencil2: 프로젝트 생성
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/d0090a4d-cbbb-4173-81c8-3906ae08127c)

- 프로젝트 선택
    - Project: Gradle-Groovy Project
    - Language : Java
    - Spring Boot : 3.x.x
- Project Metadata
    - Group : hello
    - Artifact : servlet
    - Name : servlet
    - Package name : hello.servlet
    - **Packaging : War** (JSP를 실행하기 위해서 필요하다.)
    - Java : 17
- **Dependencies : Spring Web, Lombok**  

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/48d1f1e6-fc8f-4478-a4fc-bca3bbc2d653)

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/cbbe98d9-e408-48f6-a6d4-1dcf1d2d177f)

- 롬복 적용
1. File -> Setting -> Plugins -> Lombok 검색 실행 (재시작)
2. FIle -> Setting -> Annotation Processors -> Enable annotation processing  체크 (재시작)

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/5efaa677-c881-4636-b7f6-d1cb2650f60f)

- Postman 설치
- Postman이란 API의 개발을 빠르고 쉽게, 개발된 API를 테스트할 수 있고, 팀원들 간의 공유를 할 수 있게 해주는 플랫폼


***

# :pencil2: Hello 서블릿

### 🔍 스프링 부트 서블릿 환경 구성
- 스프링 부트는 서블릿을 직접 등록해서 사용할 수 있도록 `@ServletComponentScan` 을 지원한다. 
-  `hello.servlet.ServletApplication`
``` java
package hello.servlet;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan // 서블릿 자동 등록
@SpringBootApplication
public class ServletApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServletApplication.class, args);
	}

}
```
- 서블릿 등록하기
- `hello.servlet.basic.HelloServlet`
``` java
package hello.servlet.basic;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// /hello 로 오면 이 코드가 실행이 된다.
@WebServlet(name = "helloServlet", urlPatterns = "/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("HelloServlet.service");
        System.out.println("request = " + request);
        System.out.println("response = " + response);

        String username = request.getParameter("username");
        System.out.println("username = " + username);

        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write("hello " + username);

    }
}
```

> - `@WebServlet` 서블릿 애노테이션
> - name : 서블릿 이름
> - urlPatterns : URL 매핑

- 서블릿이름이랑, URL매핑은 겹치면 안된다. 
- HTTP 요청을 통해 매핑된 URL이 호출되면 서블릿 컨테이너는 다음 메서드를 실행한다. 
`protected void service (HttpServletRequest request, HttpServletResponse response)`

- 웹 브라우저 실행
    - http://localhost:8080/hello?username=world
    - 결과 : hello world
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/582ff2d2-15f8-44bb-ba78-709304e03469)

- 콘솔 실행 결과
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/a6269cf4-d0dc-4042-9869-6eb1c90b7959)

> - 서블릿 컨테이너는 URL 에서 "/hello" 에 해당하는 서블릿을 찾는다. 
> - 해당 서블릿 (HelloServlet)의 service() 메서드가 호출이 된다. 
> - HttpServletRequest 객체에는 브라우저에 전달된 요청 정보가 담겨있다. "username=world" 같은 쿼리 문자열이 포함된다. 
> -  HttpServletRequest.getParameter("username")을 통해서 world 의 값을 가져온다.
> - HttpServletResponse 객체를 사용하여 클라이언트에게 응답을 생성한다. 
 
### 🔍 HTTP 요청 메시지 로그로 확인하기
- `application.properties` 에서 `logging.level.org.apache.coyote.http11=trace` 추가하기
- 서버를 다시 시작하고, 요청해보면 서버가 받은 HTTP 요청 메시지를 출력하는 것을 확인할 수 있음
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/a252cbc9-46ae-4c54-a2d8-f2d9f9de0a99)

📍 참고!
- 운영 서버에 이렇게 모든 요청 정보를 다 남기면 성능 저하가 발생할 수 있다. 개발 단계에서만 적용해야 한다.  


### 🔍 서블릿 컨테이너 동작 방식 설명
- 내장 톰켓 서버 생성
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/16312ce1-a0a3-4fbd-8bad-4f19587ccc9a)

> - 스프링 부트가 내장 톰켓 서버를 띄워준다. 
> - 톰켓 서버는 내부에 서블릿 컨테이너 기능을 가지고 있다. 
> - 컨테이너를 통해서 서블릿을 생성해준다. 서블릿 안에 helloServlet이 생성된다. 

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/75ca6e1e-7fd0-4883-a747-749b4bee4527)

> - 웹 브라우저에 http://localhost:8080/hello?username=world 라고 입력을 하면, http 메시지를 만들어서 전달해준다. 
> - 서버는 request, response 객체를 만들어서 싱글톤인 helloServlet을 호출해준다. 
> - 거기에 service 메소드를 호출하면서 request,response 를 넘겨준다.
> - 예시는 hello world 메시지를 넣게 되면 종료되고 나가면서 WAS 서버가 response 정보를 가지고 HTTP 응답 메시지를 만들어서 반환해준다. 
> - 참고 ) HTTP 응답에서 Content-Length 는 웹 애플리케이션 서버가 자동으로 생성해준다. 

### 🔍 welcome 페이지 추가
- `webapp`경로에 `index.html` 을 두면 http://localhost:8080 호출 시, `index.html` 페이지가 열린다. 
- `main/webapp/index.html`
``` html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<ul>
    <li><a href="basic.html">서블릿 basic</a></li>
</ul>
</body>
</html>
```

- `main/webapp/basic.html`
``` html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<ul>
    <li>hello 서블릿
        <ul>
            <li><a href="/hello?username=servlet">hello 서블릿 호출</a></li>
        </ul>
    </li>
    <li>HttpServletRequest
        <ul>
            <li><a href="/request-header">기본 사용법, Header 조회</a></li>
            <li>HTTP 요청 메시지 바디 조회
                <ul>
                    <li><a href="/request-param?username=hello&age=20">GET - 쿼리
                        파라미터</a></li>
                    <li><a href="/basic/hello-form.html">POST - HTML Form</a></li>
                    <li>HTTP API - MessageBody -> Postman 테스트</li>
                </ul>
            </li>
        </ul>
    </li>
    <li>HttpServletResponse
        <ul>
            <li><a href="/response-header">기본 사용법, Header 조회</a></li>
            <li>HTTP 응답 메시지 바디 조회
                <ul>
                    <li><a href="/response-html">HTML 응답</a></li>
                    <li><a href="/response-json">HTTP API JSON 응답</a></li>
                </ul>
            </li>
        </ul>
    </li>
</ul>
</body>
</html>
```


***

# :pencil2: HttpServletRequest - 개요

### 🔍 HttpServletRequest 역할
> 서블릿은 개발자가 HTTP 요청 메시지를 편리하게 사용할 수 있도록 개발자 대신에 HTTP 요청 메시지를 파싱한다. 그리고 그 결과를 `HttpServletRequest` 객체에 담아서 제공한다. 

- HTTP 요청 메시지
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/bcb5f064-f756-486c-9e1b-cfa4914c1e20)

> - START LINE
>     -  HTTP 메소드, URL, 쿼리 스트링, 스키마, 프로토콜
> - 헤더
>     - 헤더 조회
> - 바디
>     - form 파라미터 형식 조회, message body 데이터 직접 조회

- 임시 저장소 기능 (해당 HTTP 요청이 시작부터 끝날 때까지 유지되는 임시 저장소 기능)
    - 저장 : `request.setAttribute(name, value)`
    - 조회 : `request.getAttribute(name)`
- 세션 관리 기능
    - `request.getSession(create: true)

### ⭐ 중요
> HttpServletRequest, HttpServletResponse를 사용할 때, 이 객체들이 HTTP 요청 메시지, 응답 메시지를 편리하게 사용하도록 도와주는 객체이다. 따라서 이 기능들에 대한 깊이 있는 이해를 하려면 **HTTP 스펙이 제공하는 요청, 응답 메시지 자체를 이해해야 한다**  
 
***

# :pencil2: HttpServletRequest - 기본 사용법
`hello.servlet.basic.request.RequestHeaderServlet`
``` java
@WebServlet (name = "requestHeaderServlet", urlPatterns = "/request-header")
public class RequestHeaderServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        printStartLine(request);
        printHeaders(request);
        printHeaderUtils(request);
        printEtc(request);
    }
```

- start-line 정보
``` java
 private void printStartLine(HttpServletRequest request) {
        System.out.println("--- REQUEST-LINE - start ---");
        System.out.println("request.getMethod() = " + request.getMethod()); //GET
        System.out.println("request.getProtocol() = " + request.getProtocol()); //HTTP/1.1
        System.out.println("request.getScheme() = " + request.getScheme()); //http
        // http://localhost:8080/request-header
        System.out.println("request.getRequestURL() = " + request.getRequestURL());
        // /request-header
        System.out.println("request.getRequestURI() = " + request.getRequestURI());
        //username=hi
        System.out.println("request.getQueryString() = " +
                request.getQueryString());
        System.out.println("request.isSecure() = " + request.isSecure()); //https 사용 유무
        System.out.println("--- REQUEST-LINE - end ---");
        System.out.println();
    }
```
- 웹 브라우저에 `http://localhost:8080/request-header?username=hello` 라고 입력했을 때
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/f9b5ed49-c89a-4d9a-bc1c-ceb59fdb0d2b)

- 헤더정보
``` java
 private void printHeaders(HttpServletRequest request) {
        System.out.println("--- Headers - start ---");

        //Http에 있는 모든 헤더정보를 꺼내서 출력한다. (예전 스타일의 코딩 방식)
//        Enumeration<String> headerNames = request.getHeaderNames();
//        while (headerNames.hasMoreElements()) {
//            String headerName = headerNames.nextElement();
//            System.out.println(headerName + ": " + headerName);
//        }

        request.getHeaderNames().asIterator()
                        .forEachRemaining(headerName -> System.out.println(headerName + ": " + headerName));


        System.out.println("--- Headers - end ---");
        System.out.println();
    }
```
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/24d4cd42-f8ac-400e-8223-b56b2876a090)

- Header 편리한 조회
``` java
    private void printHeaderUtils(HttpServletRequest request) {

        System.out.println("---Header 편의 조회 start ---");
        System.out.println("[Host 편의 조회]");
        System.out.println("request.getServerName() = " +
                request.getServerName()); //Host 헤더
        System.out.println("request.getServerPort() = " +
                request.getServerPort()); //Host 헤더
        System.out.println();
        System.out.println("[Accept-Language 편의 조회]");
        request.getLocales().asIterator()
                .forEachRemaining(locale -> System.out.println("locale = " +
                        locale));
        System.out.println("request.getLocale() = " + request.getLocale());
        System.out.println();

        System.out.println("[cookie 편의 조회]");
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                System.out.println(cookie.getName() + ": " + cookie.getValue());
            }
        }
        System.out.println();

        System.out.println("[Content 편의 조회]");
        System.out.println("request.getContentType() = " + request.getContentType());
        System.out.println("request.getContentLength() = " + request.getContentLength());
        System.out.println("request.getCharacterEncoding() = " + request.getCharacterEncoding());

        System.out.println("--- Header 편의 조회 end ---");
        System.out.println();
    }
```

- 결과
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/e7d0f450-333a-4be0-b452-b0af66a3ab67)

***

# :pencil2: HTTP 요청 데이터 - 개요
- 주로 3가지 방법을 사용한다. 
- :one: **GET - 쿼리 파라미터**
    - **/url?username=hello&age=20**
    - 메시지 바디 없이, URL의 쿼리 파라미터에 데이터를 포함해서 전달
    - 예) 검색, 필터, 페이징등에서 많이 사용하는 방식
- :two: **POST - HTML Form**
    - content-type: application/x-www-form-urlencoded
    - 메시지 바디에 쿼리 파라미터 형식으로 전달 username=hello&age=20
    - 예) 회원 가입, 상품 주문, HTML Form 사용
- :three: **HTTP message body** 에 데이터를 직접 담아서 요청
    - HTTP API 에서 주로 사용, JSON, XML, TEXT
- 데이터 형식은 주로 JSON 사용
    - POST, PUT, PATCH    

- POST- HTML Form  예시
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/5b15f097-b09a-4192-92c8-0ddc84a281dd)

> - content-Type은 바디에 대한 정보를 가지고 있다. 
> - 위의 사진은 html 폼을 통해서 전달된 정보임을 알 수 있다.
***
#  :pencil2: HTTP 요청 데이터 - GET 쿼리 파라미터
- 전달 데이터
    - username=hello
    - age=20
- 메시지 바디 없이, URL의 **쿼리 파라미터**를 사용해서 데이터를 전달한다. -> 검색, 필터, 페이징 등에서 많이 사용하는 방식
- 쿼리 파라미터는 URL에 ?를 시작으로 보낼 수 있다. 추가 파라미터는 `&`로 구분한다. 
- `http://localhost:8080/request-param?username=hello&age=20`
- 서버에서 `HttpServletRequest` 가 제공하는 다음 메서드를 통해서 쿼리 파라미터를 편리하게 조회할 수 있다. 
> - String username = request.getParameter("username"); //단일 파라미터 조회
> - Enumeration<String> parameterNames = request.getParameterNames(); //파라미터 이름들 모두 조회
> - Map<String, String[]> parameterMap = request.getParameterMap(); //파라미터를 Map으로 조회
> - String[] usernames = request.getParameterValues("username"); / /복수 파라미터 조회

- `RequestParamServlet`
``` java
package hello.servlet.basic.request;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Enumeration;

/**
 * 1. 파라미터 전송 기능
 * http://localhost:8080/request-param?username=hello&age=20
 * 
 * 2. 동일한 파라미터 전송 가능
 * http://localhost:8080/request-param?username=hello&username=kim&age=20
 *
 */

@WebServlet(name = "requestParamServlet", urlPatterns = "/request-param")
public class RequestParamServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("[전체 파라미터 조회] - start");

        //paramName은 이름을 꺼내는 것 (username, age), request.getParameter(paramName) 은 그 값인 hello, age를 꺼내는 것
        request.getParameterNames().asIterator()
                        .forEachRemaining(paramName -> System.out.println(paramName + "=" + request.getParameter(paramName)));

        System.out.println("[전체 파라미터 조회] - end");
        System.out.println();

        System.out.println("[단일 파라미터 조회]");
        String username = request.getParameter("username");
        String age = request.getParameter("age");

        System.out.println("username = " + username);
        System.out.println("age = " + age);
        System.out.println();

        System.out.println("[이름이 같은 복수 파라미터 조회]");
        String[] usernames = request.getParameterValues("username");
        for (String name : usernames) {
            System.out.println("username = " + name);
        }

        response.getWriter().write("ok");

    }
}
```
- 실행 - 파라미터 전송
- `http://localhost:8080/request-param?username=hello&age=20`

- 결과
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/2a144a2e-1e16-4148-b5fa-44658eddd693)

- 실행 - 동일 파라미터 전송
- `http://localhost:8080/request-param?username=hello&username=kim&age=20`

- 결과
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/b600cdbf-72b2-4891-b395-0ccbd90756e4)

### :round_pushpin: 복수 파라미터에서 단일 파라미터 조회
> - `username=hello&username=kim` 과 같이 파라미터 이름은 하나인데, 값이 중복일 경우
> - `request.getParameter()` 는 하나의 파라미터 이름에 대해서 단 하나의 값만 있을 때 사용한다. 
> - 중복일 경우에는 `request.getParameterValues()` 를 사용한다. 
> - 중복인데, `request.getParameter()` 를 사용하는 경우는 첫 번째 값을 반환한다. 

***

#  :pencil2: HTTP 요청 데이터 - POST HTML Form
### HTML의 Form을 사용하여 클라이언트에서 서버로 데이터를 전송하자. 
- 특징
    - content-type : `application/x-www-form-urlencoded`
    - 메시지 바디에 쿼리 파라미터 형식으로 데이터를 전달한다. `username=hello&age=20`

- `src/main/webapp/basic/hello-form.html`
``` html
 <!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>Title</title>
</head>
<body>
<form action="/request-param" method="post">
  username: <input type="text" name="username" />
  age: <input type="text" name="age" />
  <button type="submit">전송</button>
</form>
</body>
</html>
```

- 실행했을 때의 화면
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/b40faab4-6f9c-4af2-839c-5cf47737b381)

- POST의 HTML Form을 전송하면 웹 브라우저는 다음 형식으로 HTTP 메시지를 만든다. 
    - **요청 URL** : http://localhost:8080/request-param
    - **content-type** : `application/x-www-form-urlencoded`
    - **message body** : `username=hello&age=20`

 - `request.getParameter()` 는 GET URL 쿼리 파라미터 형식도 지원하고 POST HTML Form 형식도 둘 다 지원한다. 

### :round_pushpin: 참고
> content-type은 HTTP 메시지 바디의 데이터 형식을 지정한다. 
> **GET URL 쿼리 파라미터 형식** 으로 클라이언트에서 서버로 데이터를 전달할 떄는 HTTP 메시지 바디를 사용하지 않기 때문에 content-type이 없다. 
> **POST HTML Form 형식** 으로 데이터를 전달하면  HTTP 메시지 바디에 해당 데이터를 포함해서 보내기 때문에 바디에 포함된 데이터가 어떤 형식인지 content-type을 꼭 지정해야 한다. 
> 이런 폼으로 데이터를 전송하는 형식을 `application/x-www-form-urlencoded` 라고 한다.

- Postman 을 사용하여 테스트를 할 수 있다. 
> - POST 전송 시 
> - Body -> `x-www-form-urlencoded` 선택
> - Headers 에서 content-type : `application/x-www-form-urlencoded` 로 지정된 부분을 확인해야한다.  
***

#   :pencil2: HTTP 요청 데이터 - API 메시지 바디 - 단순 텍스트
- HTTP message body 에 데이터를 직접 담아서 요청한다. 
- HTTP API 에서 주로 사용한다. JSON, XML, TEXT
- 데이터 형식은 주로 JSON 을 사용한다. 
- POST, PUT, PATCH 에서 사용

-RequestBodyStringServlet
``` java
package hello.servlet.basic.request;

import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "requestBodyStringServlet", urlPatterns = "/request-body-string")
public class RequestBodyStringServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletInputStream inputStream = request.getInputStream(); //메시지 바디의 내용을 byte 코드로 바로 얻을 수 있다.
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8); // byte를 문자로 변환할 때 어떤 인코딩인지 알려줘야한다.

        System.out.println("messageBody = " + messageBody);

        response.getWriter().write("ok");
    }
}
```

### :round_pushpin:  참고
> inputStream 은 byte 코드를 반환한다. byte 코드를 우리가 읽을 수 있는 문자 (String)로 보려면 Charset 을 지정해주어야 한다. 

- 문자 전송
    - POST : http://localhost:8080/request-body-string
    - content-type : text/plain
    - message body : hello
    - 결과: messagebody = hello 
***

#  :pencil2: HTTP 요청 데이터 - API 메시지 바디 - JSON
- JSON 형식으로 전송
    - POST http://localhost:8080/request-body-json
    - content-type : application/json
    - message body : `{"username" : "hello" , "age": 20}`
    - 결과 :  `message body : `{"username" : "hello" , "age": 20}`

- `hello.servlet.basic.HelloData`
``` java
package hello.servlet.basic;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class HelloData {

    private String username;
    private int age;

}
```

- `hello.servlet.basic.request.RequestBodyJsonServlet`
``` java
package hello.servlet.basic.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.servlet.basic.HelloData;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.lang.runtime.ObjectMethods;
import java.nio.charset.StandardCharsets;


@WebServlet(name = "RequestBodyJsonServlet", urlPatterns = "/request-body-json")
public class RequestBodyJsonServlet extends HttpServlet {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ServletInputStream inputStream = request.getInputStream();
        String messageBody = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);

        System.out.println("messageBody = " + messageBody);

        HelloData helloData = objectMapper.readValue(messageBody, HelloData.class);

        System.out.println("helloData.username = " + helloData.getUsername());
        System.out.println("helloData.age = " + helloData.getAge());

        response.getWriter().write("ok");
    }
}
```

- Postman 으로 실행
    - POST : http://localhost:8080/request-body-json
    - content-type : application/json (Body->raw, 가장 오른쪽 JSON 선택)
    - message body : `{"username" : "hello", "age":20}`

### :round_pushpin: 참고
> JSON 결과를 파싱해서 사용할 수 있는 자바 객체로 변환하려면 Jackson, Gson 같은 JSON 변환 라이브러리를 추가해서 사용해야 한다. 
> 스프링 부트로 Spring MVC를 선택하면 기본으로 Jackson 라이브러리 (ObjectMapper) 를 함께 제공한다.  

***

#  :pencil2: HttpServletResponse - 기본 사용법
### 🔍 HttpServletResponse 역할
- HTTP 응답 메시지 생성
    - HTTP 응답코드 지정
    - 헤더 생성
    - 바디 생성
- 편의 기능 제공
    - Content-Type, 쿠키, Redirect

### 🔍 HttpServletResponse - 기본 사용법
- `hello.servlet.basic.response.ResponseHeaderServlet`
``` java
package hello.servlet.basic.response;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "responseHeaderServlet", urlPatterns = "/response-header")
public class ResponseHeaderServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //[status-line]
        response.setStatus(HttpServletResponse.SC_OK);

        //[response-header]
        response.setHeader("Content-Type", "text/plain;charset=utf-8");
        response.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
        response.setHeader("Pragma", "no-cache");
        response.setHeader("my-header", "hello");

        //[Header 편의 메서드]
        content(response);
        cookie(response);
        redirect(response);

        //[message body]
        PrintWriter writer = response.getWriter();
        writer.println("ok");

    }
}
```

- Content 편의 메서드
``` java
private void content(HttpServletResponse response) {
        //Content-Type: text/plain;charset=utf-8
        //Content-Length: 2
        //response.setHeader("Content-Type", "text/plain;charset=utf-8");
        response.setContentType("text/plain");
        response.setCharacterEncoding("utf-8");
        //response.setContentLength(2); //(생략시 자동 생성)
    }
```
- 쿠키 편의 메서드
``` java
private void cookie(HttpServletResponse response) {
        //Set-Cookie: myCookie=good; Max-Age=600;
        //response.setHeader("Set-Cookie", "myCookie=good; Max-Age=600");
        Cookie cookie = new Cookie("myCookie", "good");
        cookie.setMaxAge(600); //600초
        response.addCookie(cookie);
    }
```

- redirect 편의 메서드
``` java
  private void redirect(HttpServletResponse response) throws IOException {
        //Status Code 302
        //Location: /basic/hello-form.html
        //response.setStatus(HttpServletResponse.SC_FOUND); //302
        //response.setHeader("Location", "/basic/hello-form.html");
        response.sendRedirect("/basic/hello-form.html");
    }
```

***

#  :pencil2: HTTP 응답 데이터 - 단순 텍스트, HTML
- HTTP 응답 메시지는 주로 아래의 내용을 담아서 전달한다. 
    - 단순 텍스트 응답 (writer.println("ok"))
    - HTML 응답
    - HTTP API - Message-Body JSON 응답

- `hello.servlet.basic.response.ResponseHtmlServlet
``` java
 package hello.servlet.basic.response;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "responseHtmmlServlet", urlPatterns = "/response-html")
public class ResponseHtmlServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Content-Type : text/html;charset=utf-8
        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        PrintWriter writer = response.getWriter();
        writer.println("<html>");
        writer.println("<body>");
        writer.println("<div>안녕?</div>");
        writer.println("</body>");
        writer.println("</html>");
    }
}
```
- HTTP 응답으로 HTML을 반환할 때는 content-type 을 `text/html` 로 지정해야 한다. 
- 실행
    - http://localhost:8080/response-html
    - 페이지 소스보기 F12 를 사용하면 결과 HTML을 확인할 수 있다. 

***

#  :pencil2: HTTP 응답 데이터 - API JSON
- hello.servlet.basic.response.ResponseJsonServlet
``` java
package hello.servlet.basic.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.servlet.basic.HelloData;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.w3c.dom.html.HTMLHeadElement;

import java.awt.*;
import java.io.IOException;

@WebServlet(name ="responseJsonServlet", urlPatterns = "/response-json")
public class ResponseJsonServlet extends HttpServlet {

    private ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //Content-Type : application/json
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        HelloData helloData = new HelloData();
        helloData.setUsername("kim");
        helloData.setAge(20);

        //{"username":"kim", "age":20}
        String result = objectMapper.writeValueAsString(helloData);
        response.getWriter().write(result);
    }
}
```
> HTTP 응답으로 JSON을 반환할 때는 content-type을 `application/json` 으로 지정해야 한다. 
> Jackson 라이브러리가 제공하는 `objectMapper.writeValueAsString()` 을 사용하면 객체를 JSON 문자로 변경할 수 있다. 
- 실행
    - http://localhost:8080/response-json

### :round_pushpin: 참고
> `application/json` 은 스펙상 utf-8 형식을 사용하도록 정의되어 있다. 
> 따라서 `application/json` 이라고 해야지 `application/json;charset=utf-8` 이라고 전달하는 것은 의미 없는 파라미터를 추가한 것이다. 
> response.getWriter() 를 사용하면 추가 파라미터를 자동으로 추가한다. 
> 이럴 경우에는 response.getOutputStream() 으로 출력하면 문제가 되지 않는다.  
***

# 🔗출처
- 김영한 - [스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1/dashboard)
***
