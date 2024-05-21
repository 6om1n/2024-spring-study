
# :star: 4. MVC 프레임워크 만들기
# ✏️ 프론트 컨트롤러 패턴 소개
- 프론트 컨트롤러 도입 전
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/7f2fa2d3-914a-4fca-9d13-a8198c8c7e5e)

>- 도입 전에는 클라이언트가 공통된 부분을 각각의 코드에서 실행을 하고, 컨트롤러를 실행시킨다. 

- 프론트 컨트롤러 도입 후
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/2f727729-643c-4b43-8a23-c6d63fa12c31)

> - 공통된 부분을 Front Controller 로 빼고, 요청에 맞는 컨트롤러를 찾아서 호출한다. 

### 🔍 FrontController 패턴 특징
- 프론트 컨트롤러 서블릿 하나로 클라이언트의 요청을 받음
- 프론트 컨트롤러가 요청에 맞는 컨트롤러를 찾아서 호출
- 입구를 하나로! 만들어 공통된 부분을 처리가 가능하다. 
- 프론트 컨트롤러를 제외한 나머지 컨트롤러는 서블릿을 사용하지 않아도 된다. 

### :mag: 스프링 웨 MVC 와 프론트 컨트롤러
- 스프링 웹 MVC의 핵심이 바로 **FrontController** 이다.
- 스프링 웹 MVC 의 **DispatcherServlet** 이 FrontController 패턴으로 구현되어 있다. 

***

# ✏️ 프론트 컨트롤러 도입 - v1
- V1 구조
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/2a444f1e-b8fb-43d6-8b99-94eb50b52ed8)

- Controller V1
``` java
package hello.servlet.web.frontcontroller.v1;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ControllerV1 {
    void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;
}
```

> - 서블릿과 비슷한 모양의 컨트롤러 인터페이스를 도입한다. 
> - 각 컨트롤러들은 이 인터페이스를 구현하면 된다. 프론트 컨트롤러는 이 인터페이스를 호출해서 구현과 관계없이 로직의 일관성을 가져갈 수 있다. 

### :mag: 인터페이스를 구현한 컨트롤러 만들기
- MemberFormControllerV1 - 회원 등록 컨트롤러
``` java
package hello.servlet.web.frontcontroller.v1.controller;

import hello.servlet.web.frontcontroller.v1.ControllerV1;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MemberFormControllerV1 implements ControllerV1 {

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String viewPath = "/WEB-INF/views/new-form.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);// controller 에서 view로 이동할 때 사용하는 것
        dispatcher.forward(request, response); // jsp를 호출하는 것
    }
}
```

- MemberSaveControllerV1 - 회원 저장 컨트롤러
``` java
package hello.servlet.web.frontcontroller.v1.controller;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.v1.ControllerV1;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MemberSaveControllerV1 implements ControllerV1 {

    private MemberRepository memberRepository = MemberRepository.getInstance();
    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        //Model에 데이터를 보관한다.
        request.setAttribute("member", member);

        String viewPath = "/WEB-INF/views/save-result.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
        
    }
}
```

- MemberListControllerV1 - 회원 목록 컨트롤러
``` java
package hello.servlet.web.frontcontroller.v1.controller;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.v1.ControllerV1;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class MemberListControllerV1 implements ControllerV1 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public void process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Member> members = memberRepository.findAll();

        request.setAttribute("members", members);

        String viewPath = "/WEB-INF/views/members.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);

    }
}
```

### :mag: 프론트 컨트롤러 만들기

- FrontControllerServletV1 - 프론트 컨트롤러
``` java
package hello.servlet.web.frontcontroller.v1;

import hello.servlet.web.frontcontroller.v1.controller.MemberFormControllerV1;
import hello.servlet.web.frontcontroller.v1.controller.MemberListControllerV1;
import hello.servlet.web.frontcontroller.v1.controller.MemberSaveControllerV1;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

// v1에 있는 어떤 것들이 들어오던 이게 실행이 된다.
@WebServlet(name="frontControllerServletV1", urlPatterns = "/front-controller/v1/*")
public class FrontControllerServletV1 extends HttpServlet {

    private Map<String, ControllerV1> controllerMap = new HashMap<>();

    public FrontControllerServletV1() {
        controllerMap.put("/front-controller/v1/members/new-form", new MemberFormControllerV1());
        controllerMap.put("/front-controller/v1/members/save", new MemberSaveControllerV1());
        controllerMap.put("/front-controller/v1/members", new MemberListControllerV1());

    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        System.out.println("FrontControllerServletV1 service");

        String requestURI = request.getRequestURI();

        ControllerV1 controller = controllerMap.get(requestURI);

        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        controller.process(request, response);

    }
}
```

### :mag: 프론트 컨트롤러 분석
- urlPatterns
    - `urlPatterns = "front-controller/v1/*" : /front-controller/v1` 를 포함한 하위 모든 요청은 이 서블릿에서 받아들인다. 
    - 예) `/front-controller/v1` , `/front-controller/v1/a` , `/front-controller/v1/a/b`
- controllerMap
    - key : 매핑 URL
    - value : 호출될 컨트롤러
- service()
> - 먼저 `requestURI` 를 조회해서 실제 호출할 컨트롤러를 `controllerMap` 에서 찾는다. 만약 없다면, 404(SC_NOT_FOUND) 상태 코드를 반환한다. 
> - 컨트롤러를 찾고 `controller.process(request, response); 을 호출해서 해당 컨트롤러를 실행한다.
- JSP
    - JSP는 이전 MVC에서 사용했던 것을 그대로 사용한다. 

- 실행
    - 등록 : http://localhost:8080/front-controller/v1/members/new-form
    - 목록 : http://localhost:8080/front-controller/v1/members
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/c278bf0a-2089-4d44-a2ea-fc4e3b254d96)

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/d5cec9fd-f74d-4b8d-a11b-ed071654aea4)
     
***

# ✏️ View 분리 - v2

- 모든 컨트롤러에서 뷰로 이동하는 부분에 중복이 있고, 깔끔하지 않다. 
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/98bc252c-1bb9-4eb5-8e87-aefd3182ed5b)

- V2 구조
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/87d01bec-85ec-41fd-b8b6-d725aed646a2)

- MyView
- 뷰 객체는 이후 다른 버전에서도 함께 사용하므로 패키지 위치를 `frontcontroller` 에 두었다. 
``` java
package hello.servlet.web.frontcontroller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MyView {

    private String viewPath;

    public MyView(String viewPath) {
        this.viewPath = viewPath;
    }

    public void render(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }
}
```

### :mag: 컨트롤러 인터페이스 만들기
- ControllerV2
``` java
package hello.servlet.web.frontcontroller.v2;

import hello.servlet.web.frontcontroller.MyView;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public interface ControllerV2 {

    MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

}
```

- MemberFormControllerV2 - 회원 등록 폼
``` java
package hello.servlet.web.frontcontroller.v2.controller;

import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v2.ControllerV2;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MemberFormControllerV2 implements ControllerV2 {

    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        return new MyView("/WEB-INF/views/new-form.jsp");
    }
}
```

> - 위와 같이 수정하므로 인해 각 컨트롤러는 `dispatcher.forward()` 를 직접 생성해서 호출하지 않아도 된다. 
> - 단순히 MyView 객체를 생성하고 거기에 뷰 이름만 넣고 반환하면 된다. 
> - `ControllerV1` 을 구현한 클래스와 `ControllerV2` 를 구현한 클래스를 비교해보면, 이 부분의 중복이 확실하게 제거된 것을 확인할 수 있다. 

- MemberSaveControllerV2 - 회원 저장
``` java
package hello.servlet.web.frontcontroller.v2.controller;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v2.ControllerV2;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MemberSaveControllerV2 implements ControllerV2 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String username = request.getParameter("username");
        int age = Integer.parseInt(request.getParameter("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        //Model에 데이터를 보관한다.
        request.setAttribute("member", member);

        return new MyView("/WEB-INF/views/save-result.jsp");
    }
}
```
- MemberListControllerV2 - 회원 목록
``` java
package hello.servlet.web.frontcontroller.v2.controller;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v2.ControllerV2;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class MemberListControllerV2 implements ControllerV2 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public MyView process(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        List<Member> members = memberRepository.findAll();

        request.setAttribute("members", members);

        return new MyView("/WEB-INF/views/members.jsp");
    }
}
```

- 프론트 컨트롤러 V2
``` java
package hello.servlet.web.frontcontroller.v2;

import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v2.controller.MemberFormControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberListControllerV2;
import hello.servlet.web.frontcontroller.v2.controller.MemberSaveControllerV2;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//front-controller
@WebServlet(name="frontControllerServletV2", urlPatterns = "/front-controller/v2/*")
public class FrontControllerServletV2 extends HttpServlet {
    private Map<String, ControllerV2> controllerMap = new HashMap<>();

    public FrontControllerServletV2() {
        controllerMap.put("/front-controller/v2/members/new-form", new MemberFormControllerV2());
        controllerMap.put("/front-controller/v2/members/save", new MemberSaveControllerV2());
        controllerMap.put("/front-controller/v2/members", new MemberListControllerV2());

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        ControllerV2 controller = controllerMap.get(requestURI);

        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        MyView view = controller.process(request, response);
        view.render(request, response);

    }
}
```

> - ControllerV2 의 반환 타입이 `MyView` 이므로 프론트 컨트롤러는 컨트롤러의 호출 결과로 `MyView` 를 반환 받는다. 
> - `view.render()` 를 호출하면 `forward` 로직을 수행해서 JSP가 실행된다. 

`MyView.render()`
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/ca83156c-f492-4fe0-a924-9e44bb7ecc62)

> - 프론트 컨트롤러의 도입으로 `MyView` 객체의 `render()` 를 호출하는 부분을 모두 일관되게 처리할 수 있다. 
> - 각각의 컨트롤러의 `MyView` 객체를 생성만 해서 반환하면 된다. 

- 실행
    - 등록 : http://localhost:8080/front-controller/v2/members/new-form
    - 목록 : http://localhost:8080/front-controller/v2/members
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/d75ab493-ca7a-488b-8909-c9e779a9e060)

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/f39f4f2c-1bbe-4968-b267-9b0444587116)
 
***

# 🔗출처
- 김영한 - [스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1/dashboard)
***