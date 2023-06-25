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
        String exception = (String)request.getAttribute("exception");

        if (exception == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println(response.getStatus() + "");
            log.error(authException.getMessage());
        }

        else if (exception.equals("refresh token not available")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println(response.getStatus() + "");
            log.error("refresh token not available");
        }

        else if (exception.equals("access token end")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            System.out.println(response.getStatus() + "");
            log.error("access token end");
        }
    }

    private void setResponse(HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
}
