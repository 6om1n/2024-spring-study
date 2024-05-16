# :star: 3. 서블릿, JSP, MVC

#  :pencil2: 회원 관리 웹 애플리케이션 요구사항 

- 회원 정보
    - 이름 : `username`
    - 나이 : `age`
- 기능 요구사항
    - 회원 저장
    - 회원 목록 조회

- 회원 도메인 모델 `hello/servlet/domain/member/Member`
``` java
package hello.servlet.domain.member;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Member {
    private Long id;
    private String username;
    private int age;

    public Member() {

    }

    public Member(String username, int age){
        this.username = username;
        this.age = age;
    }
}
```
> - id 는 Member 를 회원 저장소에 저장하면 회원 저장소가 할당된다. 

- 회원 저장소 `hello/servlet/domain/member/MemberRepository`
``` java
package hello.servlet.domain.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
* 동시성 문제가 고려되어 있지 않음, 실무에서는 ConcurrentHashMap, AtomicLong 사용 고려
 */
public class MemberRepository {

    private Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L; // id 가 하나씩 증가하는 시퀀스로 사용함

    private static final MemberRepository instance = new MemberRepository();
    //싱글톤 만들 때는 private로 생성자를 막아야한다. -> 아무나 생성하지 못하게 해야한다.
    // 무조건 이걸로 조회할 수 밖에 없다.
    public static MemberRepository getInstance() {
        return instance;
    }
    private MemberRepository() {

    }

    public Member save(Member member) {
        member.setId(++sequence);
        store.put(member.getId(), member);
        return member;
    }

    public Member findById(Long id) {
        return store.get(id);
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values()); // store 자체를 보호하기 위함
    }

    public void clearStore() {
        store.clear();
    }
}
```
> - 회원 저장소는 싱글톤 패턴을 적용했다. 
> - 스프링을 사용하면 스프링 빈으로 등록하면 되지만, 지금은 최대한 스프링 없이 순수 서블릿 만으로 구현하는 것이 목적이다. 
> - 싱글톤 패턴은 객체를 단 하나만 생성해서 공유해야 하므로 생성자를 `private` 접근자로 막아둔다. 

- 회원 저장소 테스트 코드(test) `hello/servlet/domain/member/MemberRepositoryTest`
``` java
package hello.servlet.domain.member;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

// test는 public 이 없어도 된다.
class MemberRepositoryTest {

    MemberRepository memberRepository = MemberRepository.getInstance();

    @AfterEach
    void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void save() {
        //given -> 이런게 주어졌을 때
        Member member = new Member("hello", 20);
        //when -> 이런걸 실행했을 때
        Member savedMember = memberRepository.save(member);
        //then -> 결과가 이게 나와야한다.
        Member findMember = memberRepository.findById(savedMember.getId());
        assertThat(findMember).isEqualTo(savedMember);

    }

    @Test
    void findAll() {
        //given
        Member member1 = new Member("member1", 20);
        Member member2 = new Member("member2", 30);

        memberRepository.save(member1);
        memberRepository.save(member2);
        //when

        List<Member> result = memberRepository.findAll();
        //then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result).contains(member1, member2);

    }
}
```
> - 회원을 저장하고, 목록을 조회하는 테스트를 작성했다. 
> - 각 테스트가 끝날 때, 다음 테스트에 영향을 주지 않도록 각 테스트의 저장소를 `clearStore()` 를 호출해서 초기화했다. 

***

#  :pencil2:  서블릿으로 회원 관리 웹 애플리케이션 만들기
- 서블릿으로 회원 등록 HTML 폼 제공하기
- MemberFormServlet - 회원 등록 폼 `hello/servlet/web/servlet/MemberFormServlet`
``` java
package hello.servlet.web.servlet;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "memberFormServlet", urlPatterns = "/servlet/members/new-form")
public class MemberFormServlet extends HttpServlet {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        PrintWriter w = response.getWriter();
        w.write("<!DOCTYPE html>\n" +
                "<html>\n" +
                "<head>\n" +
                " <meta charset=\"UTF-8\">\n" +
                " <title>Title</title>\n" +
                "</head>\n" +
                "<body>\n" +
                "<form action=\"/servlet/members/save\" method=\"post\">\n" +
                " username: <input type=\"text\" name=\"username\" />\n" +
                " age: <input type=\"text\" name=\"age\" />\n" +
                " <button type=\"submit\">전송</button>\n" +
                "</form>\n" +
                "</body>\n" +
                "</html>\n");

    }
}
```
> - `MemberFormServlet` 은 단순하게 회원 정보를 입력할 수 있는 HTML Form 을 만들어서 응답한다. 

-  HTML Form 에서 데이터를 입력하고 전송을 누르면 실제 회원 데이터가 저장되도록 하자.
- MemberSaveServlet - 회원 저장 `hello/servlet/web/servlet/MemberSaveServlet`
``` java
package hello.servlet.web.servlet;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet (name= "memberSaveServlet", urlPatterns = "/servlet/members/save")
public class MemberSaveServlet extends HttpServlet {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("MemberSaveServlet.service");
        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");
        PrintWriter w = response.getWriter();
        w.write("<html>\n" +
                "<head>\n" +
                " <meta charset=\"UTF-8\">\n" +
                "</head>\n" +
                "<body>\n" +
                "성공\n" +
                "<ul>\n" +
                " <li>id="+member.getId()+"</li>\n" +
                " <li>username="+member.getUsername()+"</li>\n" +
                " <li>age="+member.getAge()+"</li>\n" +
                "</ul>\n" +
                "<a href=\"/index.html\">메인</a>\n" +
                "</body>\n" +
                "</html>");
    }
}
```
> - `MemberSaveServlet` 은 다음 순서로 동작한다. 
> - 1. 파라미터를 조회해서 Member 객체를 만든다. 
> - 2. Member 객체를 MemberRepository 를 통해서 저장한다. 
> - 3. Member 객체를 사용해서 결과 화면용 HTML을 동적으로 만들어서 응답한다. 

- 실행
    - `http://localhost:8080/servlet/members/new-form`
    - 데이터가 전송되고, 저장 결과를 확인할 수 있다.
 
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/b68557e1-b38f-41eb-a9f7-625af803a249)

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/2385cafa-f11f-44a7-9322-3ed21d0e4e57)

- MemberListServlet - 회원 목록 `hello/servlet/web/servlet/MemberListServlet`
``` java
package hello.servlet.web.servlet;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet (name = "memberListServlet", urlPatterns = "/servlet/members")
public class MemberListServlet extends HttpServlet {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Member> members = memberRepository.findAll();

        response.setContentType("text/html");
        response.setCharacterEncoding("utf-8");

        PrintWriter w = response.getWriter();

        w.write("<html>");
        w.write("<head>");
        w.write("    <meta charset=\"UTF-8\">");
        w.write("    <title>Title</title>");
        w.write("</head>");
        w.write("<body>");
        w.write("<a href=\"/index.html\">메인</a>");
        w.write("<table>");
        w.write("    <thead>");
        w.write("    <th>id</th>");
        w.write("    <th>username</th>");
        w.write("    <th>age</th>");
        w.write("    </thead>");
        w.write("    <tbody>");

        /*
        w.write("    <tr>");
        w.write("        <td>1</td>");
        w.write("        <td>userA</td>");
        w.write("        <td>10</td>");
        w.write("    </tr>");
        */

        for (Member member : members) {
            w.write("    <tr>");
            w.write("        <td>" + member.getId() + "</td>");
            w.write("        <td>" + member.getUsername() + "</td>");
            w.write("        <td>" + member.getAge() + "</td>");
            w.write("    </tr>");
        }

        w.write("    </tbody>");
        w.write("</table>");
        w.write("</body>");
        w.write("</html>");

    }
}
```

- `MemberListServlet` 은 다음 순서로 동작한다. 
    - `memberRepository.findAll()` 를 통해 모든 회원을 조회한다. 
    - 회원 목록 HTML을 for 루프를 통해서 회원 수 만큼 동적으로 생성하고 응답한다. 

- 실행
    - http://localhost:8080/servlet/members
    - 저장된 회원 목록을 확인할 수 있다. 
    
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/6c8d6f46-ff9f-4d77-b492-e472bb75bef9)
  
### 📍 참고
> - 지금까지 서블릿과 자바 코드만으로 HTML 을 만들어보았다. 
> - 서블릿 덕분에 동적으로 원하는 HTML을 만들 수 있었다. 
> - 하지만, 코드는 매우 복잡하고 비효율적이다. 
> - 자바 코드로 HTML을 만들어 내는 것보다 차라리 HTML 문서에 동적으로 변경해야 하는 부분만 자바 코드를 넣을 수 있다면 더 편리할 것 이다. 
> - 이것이 바로 **템플릿 엔진** 이 나온 이유이다. **템플릿 엔진** 을 사용하면 HTML 문서에서 필요한 곳만 코드를 적용해서 동적으로 변경할 수 있다.
> - 템플릿 엔진에는 JSP, Thymeleaf, Freemarker, Velocity 등이 있다.
> - JSP 는 성능과 기능 면에서 다른 템플릿 엔진과의 경쟁에서 밀리면서, 점점 사장되어 가는 추세이다. 
> - 강의에서는 JSP 를 앞부분에서 잠깐 다루고, 스프링과 잘 통합되는 Thymeleaf 를 사용한다. 

- Welcome 페이지 변경
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
    <li>서블릿
        <ul>
            <li><a href="/servlet/members/new-form">회원가입</a></li>
            <li><a href="/sevlet/members">회원목록</a></li>
        </ul>
    </li>
    <li>JSP
        <ul>
            <li><a href="/jsp/members/new-form.jsp">회원가입</a></li>
            <li><a href="/jsp/members.jsp"> 회원목록</a></li>
        </ul>
    </li>
    <li>서블릿 MVC
        <ul>
            <li><a href="/servlet-mvc/members/new-form">회원가입</a></li>
            <li><a href="/servlet-mvc/members">회원목록</a></li>
</ul>
</li>
<li>FrontController - v1
    <ul>
        <li><a href="/front-controller/v1/members/new-form">회원가입</a></li>
        <li><a href="/front-controller/v1/members">회원목록</a></li>
    </ul>
</li>
<li>FrontController - v2
    <ul>
        <li><a href="/front-controller/v2/members/new-form">회원가입</a></li>
        <li><a href="/front-controller/v2/members">회원목록</a></li>

    </ul>
</li>
<li>FrontController - v3
    <ul>
        <li><a  href="/front-controller/v3/members/new-form">회원가입</a></li>
        <li><a  href="/front-controller/v3/members">회원목록</a></li>
    </ul>
</li>
<li>FrontController - v4
    <ul>
        <li><a href="/front-controller/v4/members/new-form">회원가입</a></li>
        <li><a href="/front-controller/v4/members">회원목록</a></li>
    </ul>
</li>
<li>FrontController - v5 - v3
    <ul>
        <li><a href="/front-controller/v5/v3/members/new-form">회원가입</a></li>
        <li><a href="/front-controller/v5/v3/members">회원목록</a></li>
    </ul>
</li>

</li>
<li>FrontController - v5 - v4
    <ul>
        <li> <a href="/front-controller/v5/v4/members/new-form">회원가입</a></li>
        <li><a  href="/front-controller/v5/v4/members">회원목록</a></li>

    </ul>
</li>

<li>SpringMVC - v1
    <ul>
        <li><a href="/springmvc/v1/members/new-form">회원가입</a></li>
        <li><a href="/springmvc/v1/members">회원목록</a></li>
    </ul>
</li>
<li>SpringMVC - v2
    <ul>
        <li><a href="/springmvc/v2/members/new-form">회원가입</a></li>
        <li><a href="/springmvc/v2/members">회원목록</a></li>
</ul>
</li>

<li>SpringMVC - v3
    <ul>
        <li><a href="/springmvc/v3/members/new-form">회원가입</a></li>
        <li><a href="/springmvc/v3/members">회원목록</a></li>
    </ul>
</li>
</ul>
</body>
</html>
```
***

#  :pencil2:  JSP로 회원 관리 웹 애플리케이션 만들기

- JSP 라이브러리 추가
- `build.gradle` 의 `dependencies` 에 추가
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/b5bbf8c8-f27d-4f8f-841d-805affcd7b77)

- 회원 등록 폼 JSP
- `main/webapp/jsp/members/new-form.jsp`
``` jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
  <meta charset="UTF-8">
  <title>Title</title>
</head>
<body>
<form action="/jsp/members/save.jsp" method="post">
  username: <input type="text" name="username" />
  age: <input type="text" name="age" />
  <button type="submit">전송</button>
</form>
</body>
</html>
```

- `<% page contentType = "text/html;charset=UTF-8" language="java" %>`
    - 첫 줄은 JSP 문서라는 뜻이다. JSP 문서는 이렇게 시작해야한다. 
> 회원 등록 폼 JSP를 보면 첫 줄을 제외하고는 완전히 HTML과 똑같다. 
> JSP는 서버 내부에서 서블릿으로 변환되는데, 이 전에 만들었던 MemberFormServlet 과 비슷한 모습으로 변환된다. 

- 회원 저장 JSP
- `main/webapp/jsp/members/save.jsp`
``` jsp
 <%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="hello.servlet.domain.member.Member" %>
<%@ page import="hello.servlet.domain.member.MemberRepository" %>

<%

    // request, response 는 사용 가능
    MemberRepository memberRepository = MemberRepository.getInstance();

    System.out.println("MemberSaveServlet.service");
    String username = request.getParameter("username");
    int age = Integer.parseInt(request.getParameter("age"));

    Member member = new Member(username, age);
    memberRepository.save(member);

%>


<html>
<head>
  <title>Title</title>
</head>
<body>
성공
<ul>
    <li> id=<%=member.getId()%></li>
    <li> username=<%=member.getUsername()%></li>
    <li> age=<%=member.getAge()%></li>
</ul>
<a href="/index.html">메인</a>
</body>
</html>
```

- JSP는 자바 코드를 그대로 다 사용할 수 있다. 
- `<%@ page import="hello.servlet.domain.member.MemberRepository" %>`
    - 자바의 import 문과 같다. 
- `<% ~~ %>`
    - 이 부분에는 자바 코드를 입력할 수 있다. 
- `<%= ~~ %>`
    - 이 부분에는 자바 코드를 출력할 수 있다. 
> - 회원 저장 JSP 를 보면, 회원 저장 서블릿 코드와 같다. 
> - 다른 점이 있다면, HTML을 중심으로 하고, 자바 코드를 부분부분 입력해주었다. 
> - `<% ~ %>`  를 사용해서 HTML 중간에 자바 코드를 출력하고 있다. 

- 회원 목록 JSP
- `main/webapp/jsp/members.jsp`
``` jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="hello.servlet.domain.member.Member" %>
<%@ page import="java.util.List" %>
<%@ page import="hello.servlet.domain.member.MemberRepository" %>

<%
 MemberRepository memberRepository = MemberRepository.getInstance();
 List<Member> members = memberRepository.findAll();
%>

 <html>
 <head>
    <meta charset="UTF-8">
    <title>Title</title>
 </head>
 <body>
 <a href="/index.html">메인</a>
 <table>
    <thead>
    <th>id</th>
    <th>username</th>
    <th>age</th>
    </thead>
    <tbody>
 <%

    for (Member member : members) {
        out.write("    <tr>");
        out.write("        <td>" + member.getId() + "</td>");
        out.write("        <td>" + member.getUsername() + "</td>");
        out.write("        <td>" + member.getAge() + "</td>");
        out.write("    </tr>");
    }
 %>
    </tbody>
 </table>
 </body>
 </html>
```   

- 실행 화면  
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/fbe1a0af-cfe1-4a4c-a08e-41bc8ef508eb)

### 🔍 서블릿과 JSP의 한계
> - 서블릿으로 개발할 때는 뷰(View) 화면을 위한 HTML을 만드는 작업이 자바 코드에 섞여서 지저분하고 복잡했다. 
> - JSP를 사용한 덕분에 뷰를 생성하는 HTML 작업을 깔끔하게 가져가고, 중간중간 동적으로 변경이 필요한 부분에만 자바 코드를 적용했다. 
> - 그러나, 코드의 상위 절반은 회원을 저장하기 위한 비즈니스 로직이고, 나머지 하위 절반만 결과를 HTML로 보여주기 위한 뷰 영역이다. 
> - 코드를 보면 JAVA 코드, 데이터를 조회하는 리포지토리 등등 다양한 코드가 모두 JSP에 노출이 되어 있다. 즉, JSP가 너무 많은 역할을 한다. 

### 🔍 MVC 패턴의 등장
- 비즈니스 로직은 서블릿처럼 다른 곳에서 처리하고, JSP는 목적에 맞게 HTML로 화면(View)를 그리는 일에 집중하자. 
- 따라서 이렇게 등장한 것이 바로 MVC 패턴이다. 
- MVC 패턴을 적용하여 프로젝트를 리팩터링 하는 것은 다음 시간에 ...

***

# 🔗출처
- 김영한 - [스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1/dashboard)
***
