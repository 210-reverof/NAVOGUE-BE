package teo.sprint.navogue.domain.memo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import teo.sprint.navogue.domain.memo.data.req.MemoAddReq;
import teo.sprint.navogue.domain.memo.data.res.MemoAddRes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

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
        List<String> keywords = getKeywords(memoAddReq.getContent());

        String originalUrl = "https://velog.io/@leemember/%EA%B0%9C%EB%B0%9C%EC%9E%90%EB%A1%9C-%EC%82%B4%EC%95%84%EA%B0%80%EB%A9%B4%EC%84%9C-%EA%BC%AD-%ED%95%9C-%EB%B2%88-%ED%95%B4%EB%B3%B4%EA%B3%A0-%EC%8B%B6%EC%97%88%EB%8D%98-%EB%82%98%ED%99%80%EB%A1%9C-%ED%95%B4%EC%99%B8-%EB%94%94%EC%A7%80%ED%84%B8-%EB%85%B8%EB%A7%88%EB%93%9C-%EB%8F%84%EC%A0%84%EA%B8%B0";
        String url = URLEncoder.encode(originalUrl, StandardCharsets.UTF_8);
        Map<String, String> openGraphData = extractOpenGraph(url);

        for (Map.Entry<String, String> entry : openGraphData.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }

        return new MemoAddRes(1);
    }

    public static Map<String, String> extractOpenGraph(String url) throws Exception {
        Document doc = Jsoup.connect(url).get();

        Elements metaTags = doc.select("meta[property^=og:]");
        Map<String, String> openGraphData = new HashMap<>();

        for (Element metaTag : metaTags) {
            String property = metaTag.attr("property");
            String content = metaTag.attr("content");
            openGraphData.put(property, content);
        }

        return openGraphData;
    }

    private List<String> getKeywords(String content) throws Exception {
        String url = "https://api.matgim.ai/54edkvw2hn/api-keyword";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-auth-token", apiKey);

        String requestBody = "{\"document\": \""+ content +"\"}";

        String responseBody = webClient.post()
                .uri(url)
                .headers(httpHeaders -> httpHeaders.addAll(headers))
                .body(BodyInserters.fromValue(requestBody))
                .retrieve()
                .bodyToMono(String.class)
                .block();

        return parseKeywords(responseBody);
    }

    private List<String> parseKeywords(String body) throws Exception {
        List<String> selectedKeywords = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(body);
        JsonNode keywordsNode = jsonNode.path("result").path("keywords");

        List<Keyword> keywords = new ArrayList<>();
        for (JsonNode keywordNode : keywordsNode) {
            Keyword keyword = new Keyword();
            keyword.setKeyword(keywordNode.get(0).asText());
            keyword.setFrequency(keywordNode.get(1).asInt());
            keywords.add(keyword);
        }

        Collections.shuffle(keywords);
        keywords.sort(Comparator.comparingInt(Keyword::getFrequency).reversed());

        int numToSelect = Math.min(5, keywords.size());
        for (int i = 0; i < numToSelect; i++) {
            selectedKeywords.add(keywords.get(i).getKeyword());
        }

        return selectedKeywords;
    }

    @Setter
    @Getter
    public class Keyword {
        private String keyword;
        private int frequency;
    }
}
