package teo.sprint.navogue.domain.user.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import teo.sprint.navogue.common.security.jwt.JwtTokenProvider;
import teo.sprint.navogue.domain.user.data.entity.User;
import teo.sprint.navogue.domain.user.repository.UserRepository;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    public String login(String code) throws Exception {
        String access_token = getAccessToken(code);
        String email = getUserInfo(access_token);

        if (userRepository.findByEmail(email) == null) {
            User user = new User(email);
            userRepository.save(user);
        }

        String accessToken = jwtTokenProvider.createAccessToken(email);
        log.info("accessToken = ", accessToken);
        return accessToken;
    }
    public String getAccessToken(String code) throws Exception {
        String access_Token = "";
        String reqURL = "https://kauth.kakao.com/oauth/token";
        String clientId = "ba988a6d89e2e66af10b99f9f7bdb97b";
        String redirectUri = "http://localhost:5173/oauth";;


        URL url = new URL(reqURL);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);


        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(conn.getOutputStream()));
        StringBuilder sb = new StringBuilder();
        sb.append("grant_type=authorization_code");

        sb.append("client_id=" + clientId);
        sb.append("&redirect_uri=" + redirectUri);

        sb.append("&code=" + code);
        bw.write(sb.toString());
        bw.flush();

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        String line = "";
        String result = "";

        while ((line = br.readLine()) != null) {
            result += line;
        }

        JsonParser parser = new JsonParser();
        JsonElement element = parser.parse(result);

        access_Token = element.getAsJsonObject().get("access_token").getAsString();

        br.close();
        bw.close();

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
