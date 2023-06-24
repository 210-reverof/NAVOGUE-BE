package teo.sprint.navogue.domain.memo.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class MemoServiceImplTest {
    @InjectMocks
    private MemoServiceImpl memberService;

    @Mock
    private RestTemplate restTemplate;

    @Test
    void API_호출() throws Exception {
        String result = memberService.getKeywords("문장을 분리하고 명사를 추출하여 빈도와 함께 반환합니다.");
        System.out.println(result);
    }
}
