package nineseven.review.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineseven.review.common.exception.ParentException;
import nineseven.review.domain.dto.CommentDto;
import nineseven.review.domain.dto.VideoListDto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SyncService {

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String YOUTUBE_API_KEY = "AIzaSyCfjV7gb-8656MZM04PJhTs9kVVSIyWZ-8";

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

    public List<CommentDto> getCommentList(String videoId){

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        HttpEntity entity = new HttpEntity<>(httpHeaders);

        ResponseEntity<Map>  responseEntity = restTemplate.exchange(
                "https://www.googleapis.com/youtube/v3/commentThreads?videoId=" + videoId + "&key=" + YOUTUBE_API_KEY + "&part=snippet" + "&maxResults=100",
                HttpMethod.GET,
                entity,
                Map.class);

        List<CommentDto> result = new ArrayList<CommentDto>();

        Map<String, Object> bodyData = (Map<String, Object>) responseEntity.getBody();
        List<Object> items = (List<Object>) bodyData.get("items");
        items.stream().forEach(item -> {
            Map<String, Object> castItem = (Map<String, Object>) item;

            Map<String, Object> snippet = (Map<String, Object>) castItem.get("snippet");
            Map<String, Object> topLevelComment = (Map<String, Object>) snippet.get("topLevelComment");
            String CommentId = (String) topLevelComment.get("id");

            Map<String, String> snippet2 = (Map<String, String>) topLevelComment.get("snippet");

            String textOriginal = (String) snippet2.get("textOriginal");

            result.add(new CommentDto(textOriginal, CommentId));
        });

        /**
         * 인공지능 서버와 통신 시작
         */
        return postCommentsListToAi(result);

    }

    public List<CommentDto> postCommentsListToAi(List<CommentDto> comments) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity entity = new HttpEntity<>(comments,httpHeaders);

        ResponseEntity<List>  responseEntity = restTemplate.exchange(
                "http://localhost:5000/getComments",
                HttpMethod.POST,
                entity,
                List.class);

        List<String> body = (List<String>)responseEntity.getBody();

        /**
         * [{'aaaa', 0,} {'bbbb', 0}, {'cccc', 1}]
         */
        List<CommentDto> result = new ArrayList<CommentDto>();

        int num = 1;
        int cnt = 0;
        String commentId = null;

        for (String comment : body) {
            if(num % 2 == 0){ // comment
                if(comment.equals("0")){
                    result.add(new CommentDto(comments.get(cnt).getComment(),commentId));
                }
                cnt++;
            }else{ // commentId
                commentId = comment;
            }
            num = num + 1;
        }

        return result;
    }

    public void deleteComment(String authToken,List<String> commentIds){
        HttpHeaders httpHeaders = new HttpHeaders();

        httpHeaders.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        httpHeaders.add("Authorization", authToken);

        HttpEntity entity = new HttpEntity(httpHeaders);

        log.info("{}", commentIds);
        commentIds.forEach(commentId -> {
            try {
                log.info("{}",commentId);
                ResponseEntity<String> responseEntity = restTemplate.exchange("https://www.googleapis.com/youtube/v3/comments/setModerationStatus?id=" + commentId
                                + "&moderationStatus=rejected"
                        , HttpMethod.POST
                        , entity
                        , String.class
                );
            } catch (HttpClientErrorException e) {
                e.printStackTrace();
                throw new ParentException();
            }

        });

    }

}
