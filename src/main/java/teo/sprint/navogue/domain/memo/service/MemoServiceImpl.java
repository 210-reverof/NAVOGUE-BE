package teo.sprint.navogue.domain.memo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import teo.sprint.navogue.domain.memo.data.req.MemoAddReq;
import teo.sprint.navogue.domain.memo.data.res.MemoAddRes;

@Service
public class MemoServiceImpl implements MemoService {
    private final WebClient webClient;

    public MemoServiceImpl(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    @Value("${api.key}")
    private String apiKey;

    @Override
    public MemoAddRes addMemo(MemoAddReq memoAddReq) throws Exception {
        String requestBody = "{\"document\": \"키워드 추출 API 입니다. 문장을 분리하고 명사를 추출하여 빈도와 함께 반환합니다.\"}";
        System.out.println(getKeywords(memoAddReq.getContent()));

        return null;
    }

    public String getKeywords(String content) {
        String url = "https://api.matgim.ai/54edkvw2hn/api-keyword";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-auth-token", apiKey);

        String requestBody = "{\"document\": \"키워드 추출 API 입니다. 문장을 분리하고 명사를 추출하여 빈도와 함께 반환합니다.\"}";

        String responseBody = webClient.post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return responseBody;
    }
}
