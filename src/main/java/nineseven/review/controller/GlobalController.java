package nineseven.review.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nineseven.review.domain.dto.GoogleOauthDto;
import nineseven.review.service.Oauth2Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class GlobalController {

    private final Oauth2Service oauth2Service;

    /**
     * https://accounts.google.com/o/oauth2/auth?client_id=835265111570-3bmt0epqvso70r1jms19hljlegmuce5j.apps.googleusercontent.com&redirect_uri=http://localhost:3001/oauth2callback&scope=https://www.googleapis.com/auth/youtube&response_type=code&access_type=offline
     */
    @GetMapping("/oauth2/code")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public GoogleOauthDto oauthCallBack(@RequestParam(name = "code", required = false) String code) throws JsonProcessingException {
        // Request Code
        GoogleOauthDto oauthDto = oauth2Service.requestOauth2Code(code);

        return oauthDto;
    }

}
