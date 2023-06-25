package teo.sprint.navogue.common.security.jwt;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String exception = (String) request.getAttribute("exception");

        if (exception == null) {
            setResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
            log.error(authException.getMessage());
            System.out.println(response.getStatus() + "");
        } else if (exception.equals("access token end")) {
            setResponse(response, HttpServletResponse.SC_UNAUTHORIZED);
            log.error("access token end");
            System.out.println(response.getStatus() + "");
        } else if (exception.equals("locked")) {
            setResponse(response, HttpServletResponse.SC_FORBIDDEN);
            log.error("사용자 계정이 잠겼습니다.");
            System.out.println(response.getStatus() + "");
            response.getWriter().write("사용자 계정이 잠겼습니다.");
            response.getWriter().flush();
            return;
        }

        response.sendError(response.getStatus());
    }

    private void setResponse(HttpServletResponse response, int statusCode) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(statusCode);
    }

}
