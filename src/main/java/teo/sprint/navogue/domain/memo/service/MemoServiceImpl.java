package teo.sprint.navogue.domain.memo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import teo.sprint.navogue.domain.memo.data.entity.Memo;
import teo.sprint.navogue.domain.memo.data.entity.OpenGraph;
import teo.sprint.navogue.domain.memo.data.req.MemoAddReq;
import teo.sprint.navogue.domain.memo.data.res.MemoAddRes;
import teo.sprint.navogue.domain.memo.repository.MemoRepository;
import teo.sprint.navogue.domain.memo.repository.OpenGraphRepository;
import teo.sprint.navogue.domain.tag.data.req.TagAddReq;
import teo.sprint.navogue.domain.tag.service.TagService;

import java.net.URL;
import java.util.*;

@Service
public class MemoServiceImpl implements MemoService {
    private final WebClient webClient;
    private final MemoRepository memoRepository;
    private final OpenGraphRepository openGraphRepository;
    private final TagService tagService;

    public MemoServiceImpl(WebClient.Builder webClientBuilder, MemoRepository memoRepository, OpenGraphRepository openGraphRepository, TagService tagService) {
        this.webClient = webClientBuilder.build();
        this.memoRepository = memoRepository;
        this.openGraphRepository = openGraphRepository;
        this.tagService = tagService;
    }

    @Value("${api.key}")
    private String apiKey;

    @Override
    public MemoAddRes addMemo(MemoAddReq memoAddReq) throws Exception {
        Memo memo = new Memo(memoAddReq);
        memo = memoRepository.save(memo);
        List<String> keywords;
        OpenGraph og;

        String target = memoAddReq.getContent();
        if (memoAddReq.getContentType().equals("URL")) {
            og = extractOpenGraph(memoAddReq.getContent());
            og.setMemo(memo);
            openGraphRepository.save(og);
            target = og.getOgTitle();
        }

        keywords = getKeywords(target);
        tagService.addTag(new TagAddReq(memo.getId(),keywords));

        return new MemoAddRes(memo.getId());
    }

    private OpenGraph extractOpenGraph(String url) throws Exception {
        Document doc = Jsoup.parse(new URL(url).openStream(), "UTF-8", url);

        Elements metaTags = doc.select("meta[property^=og:]");
        Map<String, String> openGraphData = new HashMap<>();

        for (Element metaTag : metaTags) {
            String property = metaTag.attr("property");
            String content = metaTag.attr("content");
            openGraphData.put(property, content);
        }

        OpenGraph og = new OpenGraph();
        og.setOgTitle(openGraphData.getOrDefault("og:title", ""));
        og.setOgDesc(openGraphData.getOrDefault("og:description", ""));
        og.setOgImageUrl(openGraphData.getOrDefault("og:image", ""));

        return og;
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
