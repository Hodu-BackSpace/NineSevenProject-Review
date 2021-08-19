package nineseven.review.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GoogleOauthDto {

    private String access_token;
    private Long expires_in;
    private String refresh_token;
    private String scope;
    private String token_type;

}
