
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

# :pencil2: Model 추가 - v3
- 요청 파라미터 정보는 자바의 Map 으로 대신 넘기도록 하면 지금은 컨트롤러가 서블릿 기술을 몰라도 동작할 수 있다. 
- 또한 request 객체를 Model로 사용하는 대신에 별도의 Model 객체를 만들어서 반환하면 된다. 

### :mag: 뷰 이름 중복 제거
- 컨트롤러에서 지정하는 뷰 이름에 중복이 있다. 
- 따라서 컨트롤러에서 **뷰의 논리 이름** 을 반환하고, 실제 물리 위치의 이름은 프론트 컨트롤러에서 처리하도록 단순화 해야 한다. 
- 이렇게 할 경우 향후 뷰의 폴더 위치가 함께 이동해도 프론트 컨트롤러만 고치면 된다. 
> - `WEB-INF/views/new-form.jsp` -> new form
> - `WEB-INF/views/save-result.jsp` -> save-result
> - `/WEB-INF/views/members.jsp` -> members

### :mag: v3 구조
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/6139316b-8c59-435d-b60a-5d0a3e770a53)

### :mag: ModelView
- 지금까지는 컨트롤러에서 서블릿에 종속적인 HttpServletRequest 를 사용했다. 
- 또한, Model도 `request.setAttribute()` 를 통해 데이터를 저장하고 뷰에 전달했다. 
- 이제, 서블릿의 종속성을 제거하기 위해 Model을 직접 만들고, 추가로 View 이름까지 전달하는 객체를 만들어보자. 
- `ModeView` 객체는 다른 버전에서도 사용하므로 패키지를 `frontcontroller` 에 둔다. 

- `hello/servlet/web/frontcontroller/ModelView`
``` java
package hello.servlet.web.frontcontroller;

import java.util.HashMap;
import java.util.Map;

public class ModelView {

    private String viewName;
    private Map<String, Object> model = new HashMap<>(); // 원하는 데이터를 담아둔다.

    public ModelView(String viewName) {
        this.viewName = viewName;
    }

    public String getViewName() {
        return viewName;
    }

    public void setViewName(String viewName) {
        this.viewName = viewName;
    }

    public Map<String, Object> getModel() {
        return model;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }
}
```
> - 뷰의 이름과 뷰를 렌더링할 때 필요한 model 객체를 가지고 있다. 
> - model은 단순히 map으로 되어 있으므로 컨트롤러에서 뷰에 필요한 데이터를 key, value로 넣어주면 된다. 

- `hello/servlet/web/frontcontroller/v3/ControllerV3`
``` java
package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;

import java.util.Map;

public interface ControllerV3 {

    ModelView process(Map<String, String> paramMap);
}
```
> - 컨트롤러는 서블릿 기술을 전혀 사용하지 않기에 구현이 매우 단순해지고, 테스트 코드 작성 시 테스트 하기 쉽다. 
> - HttpServletRequest 가 제공하는 파라미터는 프론트 컨트롤러가 paramMap 에 담아서 호출해주면 된다. 
> - 응답 결과로 뷰 이름과 뷰에 전달 할 Model 데이터를 포함하는 ModelView 객체를 반환하면 된다. 

- `hello/servlet/web/frontcontroller/v3/controller/MemberFormControllerV3` - 회원 등록 폼
``` java
package hello.servlet.web.frontcontroller.v3.controller;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;

import java.util.Map;

public class MemberFormControllerV3 implements ControllerV3 {

    @Override
    public ModelView process(Map<String, String> paramMap) {
        return new ModelView("new-form");
    }
}
```
- `hello/servlet/web/frontcontroller/v3/controller/MemberSaveControllerV3` - 회원 저장
``` java
package hello.servlet.web.frontcontroller.v3.controller;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import org.springframework.beans.factory.support.ManagedMap;

import java.util.Map;

public class MemberSaveControllerV3 implements ControllerV3 {


    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {
        String username = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));


        Member member = new Member(username, age);
        memberRepository.save(member);

        ModelView mv = new ModelView("save-result");
        mv.getModel().put("member", member);
        return mv;
    }
}
```

> - `paramMap.get("username");` - 파라미터 정보는 map에 담겨있다. map에서 필요한 요청 파라미터를 조회하면 된다. 
> - `mv.getModel().put("member", member); - 모델에 뷰에서 필요한 `member` 객체를 담고 반환한다. 

- `hello/servlet/web/frontcontroller/v3/controller/MemberListControllerV3` - 회원 목록
``` java
package hello.servlet.web.frontcontroller.v3.controller;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;

import java.util.List;
import java.util.Map;

public class MemberListControllerV3 implements ControllerV3 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public ModelView process(Map<String, String> paramMap) {

        List<Member> members = memberRepository.findAll();
        ModelView mv = new ModelView("members");
        mv.getModel().put("members", members);

        return mv;

    }
}
```

- `hello/servlet/web/frontcontroller/v3/FrontControllerServletV3`
``` java
package hello.servlet.web.frontcontroller.v3;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import hello.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.apache.taglibs.standard.lang.jstl.ImplicitObjects.createParamMap;

//front-controller
@WebServlet(name="frontControllerServletV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServletV3 extends HttpServlet {
    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServletV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        ControllerV3 controller = controllerMap.get(requestURI);

        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //paramMap 을 넘겨줘야한다.

        Map<String, String> paramMap = createParamMap(request);
        ModelView mv = controller.process(paramMap); // 모델을 render에 같이 넘겨줘야한다.

        String viewName = mv.getViewName(); // 논리이름 new-form
        MyView view = viewResolver(viewName);
        view.render(mv.getModel(), request, response);

    }

    // 논리주소를 물리주소로 바꾸는 함수
    private MyView viewResolver (String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    //HttpServletRequest에 있는 파라미터를 다 뽑아서 paramMap 으로 반환시킨다.
    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();

        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
```
> `createParamMap()`
> - HttpServletReqeust에서 파라미터 정보를 꺼내서 Map으로 변환한다. 그리고 해당 Map(paramMap)을 컨트롤러에 전달하면서 호출한다. 

### :mag: 뷰 리졸버
- `MyView view = viewResolver(viewName)`
- 컨트롤러가 반환한 논리 뷰 이름을 실제 물리 뷰 경로로 변경한다. 그리고, 실제 물리 경로가 있는 MyView 객체를 반환한다. 
    - 논리 뷰 이름 : `members`
    - 물리 뷰 이름 : `WEB-INF/views/members.jsp`

- `view.render(mv.getModel(), request, response)`
    -  뷰 객체를 통해서 HTML 화면을 렌더링 한다. 
    - 뷰 객체의 `render()` 는 모델 정보도 함께 받는다. 
    - JSP는 `request.getAttribute()` 로 데이터를 조회하기 때문에, 모델의 데이터를 꺼내서 `request.setAttribute()` 로 담아둔다. 
    - JSP로 포워드해서 JSP를 렌더링 한다. 

- `hello/servlet/web/frontcontroller/MyView`
``` java
package hello.servlet.web.frontcontroller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Map;

public class MyView {

    private String viewPath;

    public MyView(String viewPath) {
        this.viewPath = viewPath;
    }

    public void render(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    public void render(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        modelToRequestAttribute(model, request);
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);
        dispatcher.forward(request, response);
    }

    private void modelToRequestAttribute(Map<String, Object> model, HttpServletRequest request) {
        model.forEach((key, value) -> request.setAttribute(key, value));
    }
}
```

### 🔍 실행
- 등록 : http://localhost:8080/front-controller/v3/members/new-form
- 목록 : http://localhost:8080/front-controller/v3/members

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/fce404f2-17c5-4bc9-b4eb-a40c01fb67ca)
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/e93bae3e-8e99-4a8a-8eaf-a5478ce295ee)

***

# :pencil2: 단순하고 실용적인 컨트롤러 - v4
- 앞서 만든 v3 컨트롤러는 서블릿 종속성을 제거하고 뷰 경로의 중복을 제거한 잘 설계된 컨트롤러이다. 
- 하지만, 개발자의 입장에서 ModelView 객체를 생성하고 반환해야 하는 부분이 번거롭다. 
- 좋은 프레임워크는 아키텍처와 더불어 개발자가 단순하고 편리하게 사용할 수 있어야 한다. (실용성 !! )
- v3 를 조금 변경해서 v4 버전을 개발해보자. 

### 🔍 V4 구조
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/bb98bd72-cb03-476e-a2c3-0f38e4e09132)

> - 기본적인 구조는 v3와 동일하며, 컨트롤러가 `Modeview`를 반환하지 않고, `viewName` 만 반환한다. 

- `hello/servlet/web/frontcontroller/v4/ControllerV4`
``` java
package hello.servlet.web.frontcontroller.v4;

import java.util.Map;

public interface ControllerV4 {

    /**
     *
     * @param paramMap
     * @param model
     * @return viewname
     */
    String process(Map<String, String> paramMap, Map<String, Object> model);
}
```
> - 인터페이스에 ModelView가 없고, model 객체는 파라미터로 전달되기 때문에, 그냥 사용하고 결과로 뷰의 이름만 반환해주면 된다.

- `hello/servlet/web/frontcontroller/v4/controller/MemberFormControllerV4`
``` java
 package hello.servlet.web.frontcontroller.v4.controller;

import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v4.ControllerV4;

import java.util.Map;

public class MemberFormControllerV4 implements ControllerV4 {

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        return "new-form";
    }
}
```

- `hello/servlet/web/frontcontroller/v4/controller/MemberSaveControllerV4`
``` java
package hello.servlet.web.frontcontroller.v4.controller;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.v4.ControllerV4;

import java.util.Map;

public class MemberSaveControllerV4 implements ControllerV4 {


    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        String username = paramMap.get("username");
        int age = Integer.parseInt(paramMap.get("age"));

        Member member = new Member(username, age);
        memberRepository.save(member);

        model.put("member", member);
        return "save-result";

   }
}
```

> - `mode.put("member", member);`
> - 모델이 파라미터로 전달되기 때문에, 모델을 직접 생성하지 않아도 된다. 

- `hello/servlet/web/frontcontroller/v4/controller/MemberListControllerV4`
``` java
package hello.servlet.web.frontcontroller.v4.controller;

import hello.servlet.domain.member.Member;
import hello.servlet.domain.member.MemberRepository;
import hello.servlet.web.frontcontroller.ModelView;
import hello.servlet.web.frontcontroller.v3.ControllerV3;
import hello.servlet.web.frontcontroller.v4.ControllerV4;

import java.util.List;
import java.util.Map;

public class MemberListControllerV4 implements ControllerV4 {

    private MemberRepository memberRepository = MemberRepository.getInstance();

    @Override
    public String process(Map<String, String> paramMap, Map<String, Object> model) {
        List<Member> members = memberRepository.findAll();

        model.put("members", members);
        return "members";
    }
}
```

- `hello/servlet/web/frontcontroller/v4/FrontControllerServletV4`
``` java
package hello.servlet.web.frontcontroller.v4;

import hello.servlet.web.frontcontroller.MyView;
import hello.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import hello.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

//front-controller
@WebServlet(name="frontControllerServletV4", urlPatterns = "/front-controller/v4/*")
public class FrontControllerServletV4 extends HttpServlet {
    private Map<String, ControllerV4> controllerMap = new HashMap<>();

    public FrontControllerServletV4() {
        controllerMap.put("/front-controller/v4/members/new-form", new MemberFormControllerV4());
        controllerMap.put("/front-controller/v4/members/save", new MemberSaveControllerV4());
        controllerMap.put("/front-controller/v4/members", new MemberListControllerV4());

    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        ControllerV4 controller = controllerMap.get(requestURI);

        if (controller == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //paramMap 을 넘겨줘야한다.
        Map<String, String> paramMap = createParamMap(request);
        Map<String, Object> model = new HashMap<>(); // 추가

        String viewName = controller.process(paramMap, model);

        MyView view = viewResolver(viewName);
        view.render(model, request, response);
    }

    // 논리주소를 물리주소로 바꾸는 함수
    private MyView viewResolver (String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    //HttpServletRequest에 있는 파라미터를 다 뽑아서 paramMap 으로 반환시킨다.
    private Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();

        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
```

### :mag: 모델 객체 전달
- `Map<String, Object> model = new HashMap<>();`
- 모델 객체를 프론트 컨트롤러에서 생성해서 넘겨준다. 
- 컨트롤러에서 모델 객체에 값을 담으면 여기에 그대로 담겨있게 된다. 

### :mag: 뷰의 논리 이름을 직접 반환
``` java
     String viewName = controller.process(paramMap, model);
     MyView view = viewResolver(viewName);
```

### :mag: 실행
- 등록 : http://localhost:8080/front-controller/v4/members/new-form
- 목록 : http://localhost:8080/front-controller/v4/members

![image](https://github.com/2024-SpringStudy/spring/assets/78257436/b3ff3c8d-2387-4199-92a0-7cd7f81b3a28)
![image](https://github.com/2024-SpringStudy/spring/assets/78257436/37e412af-7050-420d-8b8f-272ad9f9bbfb)

## :loudspeaker: 정리

- 이번 컨트롤러에서는 기존 구조에서 모델을 파라미터로 넘기고, 뷰의 논리 이름을 반환하였다. 
- 이를 통해 컨트롤러를 구현하는 개발자 입장에서 보면 군더더기 없는 코드를 작성할 수 있게 되었다. 
- **프레임워크나 공통 기능이 수고로워야 사용하는 개발자가 편리해진다** !!

***

# 🔗출처
- 김영한 - [스프링 MVC 1편 - 백엔드 웹 개발 핵심 기술](https://www.inflearn.com/course/%EC%8A%A4%ED%94%84%EB%A7%81-mvc-1/dashboard)
***