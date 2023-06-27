package teo.sprint.navogue.domain.memo.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import teo.sprint.navogue.domain.memo.data.entity.ContentType;
import teo.sprint.navogue.domain.memo.data.entity.Memo;
import teo.sprint.navogue.domain.memo.data.entity.OpenGraph;
import teo.sprint.navogue.domain.memo.data.req.MemoAddReq;
import teo.sprint.navogue.domain.memo.data.req.MemoUpdateReq;
import teo.sprint.navogue.domain.memo.data.res.MemoAddRes;
import teo.sprint.navogue.domain.memo.data.res.MemoListRes;
import teo.sprint.navogue.domain.memo.repository.MemoRepository;
import teo.sprint.navogue.domain.memo.repository.MemoRepositorySupport;
import teo.sprint.navogue.domain.memo.repository.OpenGraphRepository;
import teo.sprint.navogue.domain.tag.data.req.TagAddReq;
import teo.sprint.navogue.domain.tag.repository.TagRelationRepository;
import teo.sprint.navogue.domain.tag.service.TagService;
import teo.sprint.navogue.domain.user.data.entity.User;
import teo.sprint.navogue.domain.user.repository.UserRepository;

import java.net.URL;
import java.util.*;

@Slf4j
@Service
public class MemoServiceImpl implements MemoService {
    private final WebClient webClient;
    private final MemoRepository memoRepository;
    private final UserRepository userRepository;
    private final OpenGraphRepository openGraphRepository;
    private final TagRelationRepository tagRelationRepository;
    private final MemoRepositorySupport memoRepositorySupport;
    private final TagService tagService;

    public MemoServiceImpl(WebClient.Builder webClientBuilder, MemoRepository memoRepository, UserRepository userRepository, OpenGraphRepository openGraphRepository, TagRelationRepository tagRelationRepository, MemoRepositorySupport memoRepositorySupport, TagService tagService) {
        this.webClient = webClientBuilder.build();
        this.memoRepository = memoRepository;
        this.userRepository = userRepository;
        this.openGraphRepository = openGraphRepository;
        this.tagRelationRepository = tagRelationRepository;
        this.memoRepositorySupport = memoRepositorySupport;
        this.tagService = tagService;
    }

    @Value("${api.key}")
    private String apiKey;

    @Override
    public MemoAddRes addMemo(MemoAddReq memoAddReq, String email) throws Exception {
        Memo memo = new Memo(memoAddReq);
        User user = userRepository.findByEmail(email).get();
        memo.setUser(user);
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

    @Override
    public Slice<MemoListRes> getList(int page, String type, String tag, String keyword, String email) {
        User user = userRepository.findByEmail(email).get();

        List<MemoListRes> memoList = memoRepositorySupport.getList(type, tag, keyword, user);
        log.info("memoList" + memoList);

        int pageSize = 6; // 페이지당 데이터 개수
        int totalElements = memoList.size();
        int totalPages = (int) Math.ceil((double) totalElements / pageSize);

        int fromIndex = page * pageSize;
        int toIndex = Math.min(fromIndex + pageSize, totalElements);

        List<MemoListRes> pagedMemoList = memoList.subList(fromIndex, toIndex);
        for (MemoListRes m : pagedMemoList) {
            if (m.getContentType().equals(ContentType.URL) && m.getOpenGraph() == null) {
                m.setOpenGraph(new OpenGraph(0, memoRepository.findById(m.getId()).get(), "no title", "no desc", "https://cdn.vectorstock.com/i/preview-1x/65/30/default-image-icon-missing-picture-page-vector-40546530.jpg"));
            }
        }
        boolean hasNext = page < totalPages - 1;

        return new SliceImpl<>(pagedMemoList, PageRequest.of(page, pageSize), hasNext);
    }

    @Override
    public int pin(int memoId) {
        memoRepository.pinMemo(memoId);
        return memoId;
    }

    @Override
    public int delete(int memoId) {
        tagRelationRepository.deleteByMemoId(memoId);
        openGraphRepository.deleteByMemoId(memoId);
        memoRepository.deleteById(memoId);
        return memoId;
    }

    @Override
    public int update(MemoUpdateReq memoUpdateReq) throws Exception {
        if (memoRepository.findById(memoUpdateReq.getId()).get().getContentType().equals(ContentType.URL)) {
            openGraphRepository.deleteByMemoId(memoUpdateReq.getId());
            OpenGraph og = extractOpenGraph(memoUpdateReq.getContent());
            openGraphRepository.save(og);
        }
        memoRepository.updateContent(memoUpdateReq.getId(), memoUpdateReq.getContent());
        return memoUpdateReq.getId();
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
