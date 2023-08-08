package org.wakeupu.wakeupu.controller;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wakeupu.wakeupu.dto.member.MemberResponseDto;
import org.wakeupu.wakeupu.dto.sign.LoginRequestDto;
import org.wakeupu.wakeupu.dto.sign.SignupRequestDto;
import org.wakeupu.wakeupu.dto.sign.TokenRequestDto;
import org.wakeupu.wakeupu.dto.token.TokenDto;

import org.wakeupu.wakeupu.service.SignService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sign")
public class SignController {

    private final SignService signService;

    //회원 가입
    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto> signup(@Valid @RequestBody SignupRequestDto signupRequestDto) {
        return ResponseEntity.ok(signService.signup(signupRequestDto));

    }

    //로그인
    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        return ResponseEntity.ok(signService.login(loginRequestDto));
    }

    //로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request){
        signService.logout(request);
        return ResponseEntity.ok("로그아웃 성공");
    }

    //회원 탈퇴
    @DeleteMapping()
    public ResponseEntity<String> deleteMember() {
        signService.deleteMember();
        return ResponseEntity.ok("회원 탈퇴 성공");
    }

    //토큰 재발급
    @PostMapping("/reissue")
    public ResponseEntity<TokenDto> reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return ResponseEntity.ok(signService.reissue(tokenRequestDto));
    }

}
