package univ.yesummit.global.resolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import univ.yesummit.domain.member.exception.MemberException;
import univ.yesummit.global.auth.util.JwtUtils;
import univ.yesummit.global.exception.ErrorCode;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthArgumentResolver implements HandlerMethodArgumentResolver {

    private final JwtUtils jwtUtils;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(User.class)
                && parameter.getParameterType().equals(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        jwtUtils.extractAccessToken(Objects.requireNonNull(request))
                .orElseThrow(() -> new MemberException(ErrorCode.NOT_FOUND_ACCESS_TOKEN));

        Long memberId = jwtUtils.extractMemberId(request)
                .orElseThrow(() -> new MemberException(ErrorCode.INVALID_ACCESS_TOKEN));
        return new LoginUser(memberId);
    }
}
