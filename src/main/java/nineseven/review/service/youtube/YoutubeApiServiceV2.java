package nineseven.review.service.youtube;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineseven.review.domain.dto.VideoListDto;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class YoutubeApiServiceV2{

    private final RestTemplate restTemplate = new RestTemplate();

    public List<VideoListDto> getUserChannelId(String authToken) {
        String access_token = authToken;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", access_token);

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<Map> responseEntity = restTemplate.exchange("https://www.googleapis.com/youtube/v3/channels?mine=true&part=contentDetails&part=snippet", HttpMethod.GET,
                entity, Map.class);

        Map<String, Object> result = responseEntity.getBody();

        List<Map<String,String>> items = (List<Map<String,String>>) result.get("items");
        Map<String, String> itemsListMap = items.get(0);
        String id = itemsListMap.get("id");

        return getUserVideos(id, authToken);
    }


    public List<VideoListDto> getUserVideos(String channelId,String authToken) {
        String access_token = authToken;
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", access_token);

        HttpEntity entity = new HttpEntity(headers);

        ResponseEntity<Map> responseEntity = restTemplate.exchange("https://www.googleapis.com/youtube/v3/search?channelId=" + channelId + "&part=snippet", HttpMethod.GET,
                entity, Map.class);

        List<VideoListDto> resultList = new ArrayList<>();

        Map<String, Object> result = responseEntity.getBody();
        List<Map<String, Map<String, String>>> items = (List<Map<String, Map<String, String>>>) result.get("items");

        List<Map<String, Map<String, String>>> splitList = items.subList(1, items.size());
        splitList.iterator().forEachRemaining(item -> {
            VideoListDto videoListDto = new VideoListDto(item.get("id").get("videoId"), item.get("snippet").get("title"), item.get("snippet").get("description"));
            resultList.add(videoListDto);
        });

        return resultList;
    }
}
