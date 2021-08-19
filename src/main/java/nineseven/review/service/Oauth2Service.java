package nineseven.review.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineseven.review.domain.dto.GoogleOauthDto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class Oauth2Service {

    private final static String GOOGLE_CLIENT_ID = "812957670514-e7arbd2p80s7f7ln233da9r716934u5h.apps.googleusercontent.com";
    private final static String GOOGLE_CLIENT_SECRET = "xO6hhyiK3Da5zA_tyKTLNFUk";
    private final static String GOOGLE_REDIRECT_URI = "http://localhost:3000/oauth2callback";
    private final static String GOOGLE_GRANT_TYPE = "authorization_code";
    private final ObjectMapper objectMapper = new ObjectMapper();


    public GoogleOauthDto requestOauth2Code(String code) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> data = new LinkedMultiValueMap<>();
        data.put("code", List.of(code));
        data.put("client_id", List.of(GOOGLE_CLIENT_ID));
        data.put("client_secret", List.of(GOOGLE_CLIENT_SECRET));
        data.put("redirect_uri", List.of(GOOGLE_REDIRECT_URI));
        data.put("grant_type", List.of(GOOGLE_GRANT_TYPE));

        HttpEntity entity = new HttpEntity(data, httpHeaders);


        ResponseEntity<String> responseEntity = restTemplate.exchange("https://accounts.google.com/o/oauth2/token", HttpMethod.POST,
                entity, String.class);

        GoogleOauthDto oauthDto = objectMapper.readValue(responseEntity.getBody(), GoogleOauthDto.class);

        // TODO : 유저 정보 가져온 후, 유저 정보 DB에 등록

        return oauthDto;

    }

//    /**
//     * https://www.googleapis.com/userinfo/v2/me?access_token=
//     * @param oauthDto
//     */
//    public void getUserInfo(GoogleOauthDto oauthDto) {
//        String TOKEN_INFO = oauthDto.getAccess_token();
//
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders httpHeaders = new HttpHeaders();
//
//        HttpEntity httpEntity = new HttpEntity(httpHeaders);
//
//        ResponseEntity<String> responseEntity = restTemplate.exchange("https://www.googleapis.com/userinfo/v2/me?credential=" + TOKEN_INFO, HttpMethod.GET,
//                httpEntity, String.class);
//
//        log.info(responseEntity.getBody());
//    }
}
