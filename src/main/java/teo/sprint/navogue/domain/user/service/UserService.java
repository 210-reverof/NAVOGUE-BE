package teo.sprint.navogue.domain.user.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import teo.sprint.navogue.common.security.jwt.JwtTokenProvider;
import teo.sprint.navogue.domain.user.data.entity.User;
import teo.sprint.navogue.domain.user.data.response.UserLoginRes;
import teo.sprint.navogue.domain.user.repository.UserRepository;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${kakao.clientId}")
    private String kakaoClientId;

    @Value("${kakao.redirectUri}")
    private String kakaoRedirectUri;

    public UserLoginRes login(String code) throws Exception {
        System.out.println("======================== 인가코드 사용");
        String access_token = getAccessToken(code);
        String email = getUserInfo(access_token);


        String accessToken = jwtTokenProvider.createAccessToken(email);

        if (!userRepository.existsByEmail(email)) {
            User user = new User(email);
            User saveUser = userRepository.save(user);
            return new UserLoginRes(saveUser.getId(), accessToken);
        }
        User user = userRepository.findByEmail(email).get();
        log.info("accessToken = ", accessToken);
        return new UserLoginRes(user.getId(), accessToken);
    }
    public String getAccessToken(String code) throws Exception {
        String access_Token = "";
        String url = "https://kauth.kakao.com/oauth/token";
        String grantType = "authorization_code";
        String clientId = URLEncoder.encode(kakaoClientId, "UTF-8");
        String redirectUri = URLEncoder.encode(kakaoRedirectUri, "UTF-8");
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
        String jsonResponse = response.toString();

        int startIndex = jsonResponse.indexOf("\"access_token\":\"") + 16;
        int endIndex = jsonResponse.indexOf("\"", startIndex);
        access_Token = jsonResponse.substring(startIndex, endIndex);

        log.info("access_token = " +  access_Token);
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
        log.info("kakao_account = " + kakao_account);
        String email = kakao_account.getAsJsonObject().get("email").getAsString();

        log.info("email = ", email);
        return email;
    }
}
