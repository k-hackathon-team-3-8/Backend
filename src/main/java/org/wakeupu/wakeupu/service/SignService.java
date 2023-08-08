package org.wakeupu.wakeupu.service;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wakeupu.wakeupu.Repository.MemberRepository;
import org.wakeupu.wakeupu.Repository.RefreshTokenRepository;
import org.wakeupu.wakeupu.config.SecurityUtil;
import org.wakeupu.wakeupu.config.jwt.TokenProvider;
import org.wakeupu.wakeupu.dto.member.MemberResponseDto;
import org.wakeupu.wakeupu.dto.sign.LoginRequestDto;
import org.wakeupu.wakeupu.dto.sign.SignupRequestDto;
import org.wakeupu.wakeupu.dto.token.TokenDto;
import org.wakeupu.wakeupu.dto.sign.TokenRequestDto;
import org.wakeupu.wakeupu.entity.member.Member;
import org.wakeupu.wakeupu.entity.member.RefreshToken;


@Service
@RequiredArgsConstructor
public class SignService {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final StringRedisTemplate redisTemplate;


    @Transactional
    public MemberResponseDto signup(SignupRequestDto signupRequestDto) {
        if (memberRepository.existsByEmail(signupRequestDto.getEmail())) {
            throw new RuntimeException("이미 가입되어 있는 유저입니다");
        }

        Member member =signupRequestDto.toMember(passwordEncoder);
        return MemberResponseDto.toDto(memberRepository.save(member));
    }

    @Transactional
    public TokenDto login(LoginRequestDto loginRequestDto) {

        UsernamePasswordAuthenticationToken authenticationToken = loginRequestDto.toAuthentication();
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken refreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();

        refreshTokenRepository.save(refreshToken);

        return tokenDto;
    }

    @Transactional
    public void logout(HttpServletRequest request) {

        String jwt = request.getHeader("Authorization").substring(7);
        ValueOperations<String, String> logoutValueOperations = redisTemplate.opsForValue();
        logoutValueOperations.set(jwt, jwt);

        refreshTokenRepository.deleteByKey(String.valueOf(SecurityUtil.getCurrentMemberId()))
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }

    @Transactional
    public void deleteMember() {
        Long loginMemberId = SecurityUtil.getCurrentMemberId();

        if (loginMemberId == null) {
            throw new RuntimeException("로그인 유저 정보가 없습니다.");
        }
        memberRepository.deleteById(loginMemberId);
    }

    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {

        if (!tokenProvider.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        Authentication authentication = tokenProvider.getAuthentication(tokenRequestDto.getAccessToken());

        RefreshToken refreshToken = refreshTokenRepository.findByKey(authentication.getName())
                .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));


        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(authentication);

        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        return tokenDto;
    }
}