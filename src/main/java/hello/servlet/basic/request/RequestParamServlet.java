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
 */

/* 문제 해결할 것 - 한글 인코딩이 안먹히는 문제점이 있음*/
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

        // get 쿼리 파라미터 - 단일 파라미터 조회부터 강의 듣기 (5분부터)
    }
}
