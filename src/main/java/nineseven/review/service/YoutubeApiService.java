package nineseven.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineseven.review.domain.dto.YoutubeApiResultDto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class YoutubeApiService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String YOUTUBE_API_KEY = "AIzaSyDjVXl73h9ObAgklCWvE_mPIuVy6chS57A";

    public int getViewCount(String videoId) {
        HttpHeaders httpHeaders = new HttpHeaders();

        List<MediaType> acceptHeader = new ArrayList<>();
        acceptHeader.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(acceptHeader);

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(multiValueMap,httpHeaders);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                "https://www.googleapis.com/youtube/v3/videos?id=" + videoId + "&key=" + YOUTUBE_API_KEY+ "&part=statistics",
                HttpMethod.GET,
                entity,
                Map.class);

        Map<String, Object> bodyData = (Map<String, Object>)responseEntity.getBody();

        List<Object> items = (List<Object>) bodyData.get("items");
        Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) items.get(0);
        String viewCountString = map.get("statistics").get("viewCount");

        return Integer.parseInt(viewCountString);
    }

    /**
     * https://www.googleapis.com/youtube/v3/channels?
     * id=UCCv0FlNbZYXlnP9Mt8dXeLQ&
     * key=AIzaSyDjVXl73h9ObAgklCWvE_mPIuVy6chS57A&
     * part=snippet&part=statistics
     */
    public String getChannelInfo(String channelId) {

        HttpHeaders httpHeaders = new HttpHeaders();

        List<MediaType> acceptHeader = new ArrayList<>();
        acceptHeader.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(acceptHeader);

        /**
         * https://www.googleapis.com/youtube/v3/search?
         * part=snippet&
         * order=relevance&
         * q=%EC%86%90%ED%9D%A5%EB%AF%BC&
         * key=AIzaSyDjVXl73h9ObAgklCWvE_mPIuVy6chS57A&
         * maxResults=20
         */
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(multiValueMap,httpHeaders);

        ResponseEntity<Map> responseEntity = restTemplate.exchange(
                "https://www.googleapis.com/youtube/v3/channels?id=" + channelId + "&key=" + YOUTUBE_API_KEY+ "&part=snippet&part=statistics",
                HttpMethod.GET,
                entity,
                Map.class);

        Map<String, Object> bodyData = responseEntity.getBody();
        List<Object> items = (List<Object>) bodyData.get("items");
        Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) items.get(0);
        String subscriberCount = map.get("statistics").get("subscriberCount");

        if (!StringUtils.hasText(subscriberCount)) {
            return "hidden";
        }

        return subscriberCount;
    }

    public List<YoutubeApiResultDto> getResultList(String query) {
        //String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);

        HttpHeaders httpHeaders = new HttpHeaders();

        List<MediaType> acceptHeader = new ArrayList<>();
        acceptHeader.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(acceptHeader);

        /**
         * https://www.googleapis.com/youtube/v3/search?
         * part=snippet&
         * order=relevance&
         * q=%EC%86%90%ED%9D%A5%EB%AF%BC&
         * key=AIzaSyDjVXl73h9ObAgklCWvE_mPIuVy6chS57A&
         * maxResults=20
         */
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(multiValueMap, httpHeaders);

        ResponseEntity<Map> responseEntity = restTemplate.exchange("https://www.googleapis.com/youtube/v3/search?part=snippet&order=relevance&q=" + query+ "&key=AIzaSyDjVXl73h9ObAgklCWvE_mPIuVy6chS57A&maxResults=5",
                HttpMethod.GET,
                entity,
                Map.class);

        List<YoutubeApiResultDto> result = new ArrayList<>();
        Map<String, Object> body = responseEntity.getBody();
        List<Object> items = (List<Object>) body.get("items");
        items.stream().forEach(item -> {
            Map<String, Object> castItem = (Map<String, Object>) item;

            // Get Video ID
            Map<String, String> idMap = (Map<String, String>) castItem.get("id");
            String videoId = idMap.get("videoId");
            Map<String, String> snippetItem = (Map<String, String>) castItem.get("snippet");
            String channelId = snippetItem.get("channelId");
            String channelTitle = snippetItem.get("channelTitle");
            String videoTitle = snippetItem.get("title");

            int viewCount = getViewCount(videoId);
            String subscriberCount = getChannelInfo(channelId);
            result.add(YoutubeApiResultDto.builder()
                    .videoTitle(videoTitle)
                    .videoId(videoId)
                    .channelTitle(channelTitle)
                    .viewCount(viewCount)
                    .subscriberCount(subscriberCount)
                    .build());
        });

        log.info("{}",result.size());

        return result;
    }
}
