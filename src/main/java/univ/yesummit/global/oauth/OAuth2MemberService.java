package univ.yesummit.global.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import univ.yesummit.domain.member.entity.Member;
import univ.yesummit.domain.member.repository.MemberRepository;
import univ.yesummit.global.oauth.dto.KakaoResponse;
import univ.yesummit.global.oauth.dto.OAuth2Response;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2MemberService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("memberInfo = {}", oAuth2User.getAttributes());

        OAuth2Response oAuth2Response = getOAuth2Response(userRequest, oAuth2User)
                .orElseThrow(() -> new OAuth2AuthenticationException(
                        "Unsupported provider: " + userRequest.getClientRegistration().getRegistrationId()));

        Member member = getOrSave(oAuth2Response);

        return OAuth2Member.from(member);
    }

    private Optional<OAuth2Response> getOAuth2Response(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {

        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2Response oAuth2Response;

        if ("kakao".equals(registrationId)) {
            oAuth2Response = new KakaoResponse(oAuth2User.getAttributes());
        } else {
            throw new OAuth2AuthenticationException("Unsupported provider: " + registrationId);
        }

        return Optional.of(oAuth2Response);
    }

    private Member getOrSave(OAuth2Response oAuth2Response) {
        Member member = memberRepository.findByEmail(oAuth2Response.getEmail())
                .orElseGet(oAuth2Response::toEntity);

        return memberRepository.save(member);
    }
}
