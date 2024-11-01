package univ.yesummit.global.oauth.dto;

import lombok.RequiredArgsConstructor;
import univ.yesummit.domain.member.entity.Authority;
import univ.yesummit.domain.member.entity.Member;

import java.util.Map;

@RequiredArgsConstructor
public class KakaoResponse implements OAuth2Response {

    private final Map<String, Object> attributes;

    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }

    @Override
    public String getEmail() {
        return (String) ((Map<?, ?>) attributes.get("kakao_account")).get("email");
    }

    @Override
    public String getName() {
        return (String) ((Map<?, ?>) attributes.get("properties")).get("nickname");
    }

    @Override
    public Member toEntity() {
        return Member.builder()
                .provider(getProvider())
                .providerId(getProviderId())
                .email(getEmail())
                .username(getName())
                .authority(Authority.USER)
                .build();
    }
}
