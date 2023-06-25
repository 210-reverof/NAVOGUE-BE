package teo.sprint.navogue.domain.user.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;
import teo.sprint.navogue.common.security.jwt.JwtTokenProvider;
import teo.sprint.navogue.domain.user.data.entity.User;
import teo.sprint.navogue.domain.user.repository.UserRepository;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(String code) throws Exception {
        String access_token = getAccessToken(code);
        //String access_token = "vvp-Po2afSR_X3yBbbAM0pXvcdq1vAegarlzMcNyCj1zmwAAAYjvFLgW";
        String email = getUserInfo(access_token);
        System.out.println("==========" + access_token + "   " + email);

        if (userRepository.findByEmail(email).isEmpty()) {
            User user = new User(email);
            userRepository.save(user);
        }

        String accessToken = jwtTokenProvider.createAccessToken(email);
        log.info("accessToken = ", accessToken);
        return accessToken;
    }
    public String getAccessToken(String code) throws Exception {
        String access_Token = "";
        String url = "https://kauth.kakao.com/oauth/token";
        String grantType = "authorization_code";
        String clientId = URLEncoder.encode("b75280e464d73acb9af70f0fb5027d0c", "UTF-8");
        String redirectUri = URLEncoder.encode("http://localhost:8082/auth/login", "UTF-8");
        String code1 = URLEncoder.encode(code, "UTF-8");

        String params = String.format("grant_type=%s&client_id=%s&redirect_uri=%s&code=%s",
                grantType, clientId, redirectUri, code1);

        URL obj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();

        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

        connection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
        wr.writeBytes(params);
        wr.flush();
        wr.close();

        int responseCode = connection.getResponseCode();
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        System.out.println("Response Code: " + responseCode);
        System.out.println("Response Body: " + response.toString());
        String jsonResponse = response.toString();

        // TODO :: 파싱하기 {"access_token":"bqPA-y1fSjDQTmb2HBggSPp3YM1rdxGh7N7gczXNCj10mQAAAYjvGrOj","token_type":"bearer","refresh_token":"OSusG25A9bI2GRcUkj8EXkcm0_4h5AZcadfGk1qtCj10mQAAAYjvGrOh","expires_in":7199,"scope":"account_email","refresh_token_expires_in":5183999}
        String accessTokenStart = "\"access_token\":\"";
        int accessTokenStartIndex = jsonResponse.indexOf(accessTokenStart);
        if (accessTokenStartIndex != -1) {
            int accessTokenEndIndex = jsonResponse.indexOf("\"", accessTokenStartIndex + accessTokenStart.length());
            if (accessTokenEndIndex != -1) {
                String accessToken = jsonResponse.substring(accessTokenStartIndex + accessTokenStart.length(), accessTokenEndIndex);
                access_Token = accessToken;
            }
        }

        log.info("access_token = ", access_Token);
        return access_Token;
    }


    public String getUserInfo(String access_Token) throws Exception {
        String reqURL = "https://kapi.kakao.com/v2/user/me";

        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        conn.setRequestProperty("Authorization", "Bearer " + access_Token);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(result);

        JsonObject kakao_account = element.getAsJsonObject().get("kakao_account").getAsJsonObject();

        String email = kakao_account.getAsJsonObject().get("email").getAsString();

        log.info("email = ", email);
        return email;
    }
}
