package teo.sprint.navogue.domain.user.data.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRes {

    private Long userId;
    private String accessToken;

    public UserLoginRes(Long userId, String accessToken) {
        this.userId = userId;
        this.accessToken = accessToken;
    }
}
