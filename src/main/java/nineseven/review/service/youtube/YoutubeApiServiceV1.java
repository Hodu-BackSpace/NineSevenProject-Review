package nineseven.review.service.youtube;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineseven.review.domain.dto.YoutubeApiAiDto;
import nineseven.review.domain.dto.YoutubeApiResultDto;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class YoutubeApiServiceV1{

    private final RestTemplate restTemplate = new RestTemplate();
    private static final String YOUTUBE_API_KEY = "AIzaSyCfjV7gb-8656MZM04PJhTs9kVVSIyWZ-8";

    public int getViewCount(String videoId) {
        HttpHeaders httpHeaders = new HttpHeaders();

        List<MediaType> acceptHeader = new ArrayList<>();
        acceptHeader.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(acceptHeader);

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(multiValueMap,httpHeaders);
        try {
            ResponseEntity<Map> responseEntity = restTemplate.exchange(
                    "https://www.googleapis.com/youtube/v3/videos?id=" + videoId + "&key=" + YOUTUBE_API_KEY + "&part=statistics",
                    HttpMethod.GET,
                    entity,
                    Map.class);

            Map<String, Object> bodyData = (Map<String, Object>) responseEntity.getBody();

            List<Object> items = (List<Object>) bodyData.get("items");
            if (items.size() > 0) {
                Map<String, Map<String, String>> map = (Map<String, Map<String, String>>) items.get(0);
                String viewCountString = map.get("statistics").get("viewCount");

                if (StringUtils.hasText(viewCountString)) {
                    return Integer.parseInt(viewCountString);
                } else {
                    return -1;
                }
            } else {

                return -1;
            }
        } catch (Exception e) {
            System.out.println("e = " + e);
        }
        return -1;
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

    public void writeFile(List<YoutubeApiAiDto> result) throws IOException {
        String filePath = "/Users/wk30815/data_3.txt";
        File file = new File(filePath);


        file.createNewFile();

        if(file.canWrite() && file.exists()) {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            bufferedWriter.write("videoId   videoTitle  viewCount   subscriberCount");
            bufferedWriter.newLine();

            result.stream().forEach(dto -> {
                try {
                    bufferedWriter.write(dto.getVideoId() + "\t" + dto.getVideoTitle() + "\t" + dto.getViewCount() + "\t" + dto.getSubscriberCount());
                    bufferedWriter.newLine();
                } catch (IOException e) {
                    System.out.println("File save failed!");
                }
            });

            bufferedWriter.close();
        }
    }

    public void getJsonList(String[] queries) throws IOException {
        HttpHeaders httpHeaders = new HttpHeaders();

        List<MediaType> acceptHeader = new ArrayList<>();
        acceptHeader.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(acceptHeader);

        /**
         * videoId videoTitle viewCount subscriberCount(o)
         * search query -> get channelId
         * channels?id = channelId -> get subscriberCount
         * playlists?channelId = channelId -> get playlists[] -> get playlistId
         * playlistitems?playlistId = playListId -> get videoId, get videoTitle
         * videos?id = videoId -> getViewCount
         */
        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(multiValueMap, httpHeaders);
        List<YoutubeApiAiDto> result = new ArrayList<>();

        Arrays.stream(queries).forEach(query -> {
            ResponseEntity<Map> responseEntity = restTemplate.exchange("https://www.googleapis.com/youtube/v3/search?part=snippet&order=relevance&q=" + query+ "&key=" + YOUTUBE_API_KEY +"&maxResults=100",
                    HttpMethod.GET,
                    entity,
                    Map.class);

            Map<String, Object> body = responseEntity.getBody();
            List<Object> items = (List<Object>) body.get("items");
            items.stream().forEach(item -> {
                Map<String, Object> castItem = (Map<String, Object>) item;

                Map<String, String> snippetItem = (Map<String, String>) castItem.get("snippet");
                String channelId = snippetItem.get("channelId");

                List<String> playlists = getPlaylistId(channelId);

                playlists.stream().forEach(playlist -> {
                    getPlayListInfo(playlist,result,channelId);
                });

            });
        });

        writeFile(result);
    }

    public void getPlayListInfo(String playlistId,List<YoutubeApiAiDto> result,String channelId){
        HttpHeaders httpHeaders = new HttpHeaders();

        List<MediaType> acceptHeader = new ArrayList<>();
        acceptHeader.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(acceptHeader);

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(multiValueMap, httpHeaders);

        ResponseEntity<Map> responseEntity = restTemplate.exchange("https://www.googleapis.com/youtube/v3/playlistItems?playlistId=" + playlistId + "&key=" + YOUTUBE_API_KEY + "&part=snippet",
                HttpMethod.GET,
                entity,
                Map.class);

        Map<String, Object> body = responseEntity.getBody();
        List<Object> items = (List<Object>) body.get("items");
        items.stream().forEach(item -> {
            Map<String, Object> castItem = (Map<String, Object>) item;

            Map<String, Object> snippet = (Map<String, Object>) castItem.get("snippet");
            Map<String, String> resourceId = (Map<String, String>) snippet.get("resourceId");
            String videoId = resourceId.get("videoId");
            String videoTitle = (String) snippet.get("title");

            int viewCount = getViewCount(videoId);
            String subscriberCount = getChannelInfo(channelId);
            result.add(new YoutubeApiAiDto(videoId,videoTitle,viewCount,subscriberCount));
        });

    }

    public List<String> getPlaylistId(String channelId){
        HttpHeaders httpHeaders = new HttpHeaders();

        List<MediaType> acceptHeader = new ArrayList<>();
        acceptHeader.add(MediaType.APPLICATION_JSON);
        httpHeaders.setAccept(acceptHeader);

        MultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap<>();
        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(multiValueMap, httpHeaders);

        // result
        List<String> playlists = new ArrayList<>();

        ResponseEntity<Map> responseEntity = restTemplate.exchange("https://www.googleapis.com/youtube/v3/playlists?channelId=" + channelId + "&key=" + YOUTUBE_API_KEY + "&part=snippet",
                HttpMethod.GET,
                entity,
                Map.class);

        Map<String, Object> body = responseEntity.getBody();
        List<Object> items = (List<Object>) body.get("items");
        items.stream().forEach(item -> {
            Map<String, String> castItem = (Map<String, String>) item;

            String playListId = castItem.get("id");

            playlists.add(playListId);

        });

        return playlists;
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


        ResponseEntity<Map> responseEntity = restTemplate.exchange("https://www.googleapis.com/youtube/v3/search?part=snippet&order=relevance&q=" + query+ "&key=" + YOUTUBE_API_KEY + "&maxResults=5",
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
