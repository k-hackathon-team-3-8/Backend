package org.wakeupu.wakeupu.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.wakeupu.wakeupu.Repository.MemberRepository;
import org.wakeupu.wakeupu.config.SecurityUtil;
import org.wakeupu.wakeupu.dto.member.MemberResponseDto;
import org.wakeupu.wakeupu.dto.member.MemberUpdateDto;
import org.wakeupu.wakeupu.entity.member.Member;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public  MemberResponseDto getMyInfo() {
       return memberRepository.findById(SecurityUtil.getCurrentMemberId())
                .map(MemberResponseDto::toDto)
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));
    }

    public MemberResponseDto updateMyInfo(MemberUpdateDto memberUpdateDto) {
        Member member = memberRepository
                .findById(SecurityUtil.getCurrentMemberId())
                .orElseThrow(() -> new RuntimeException("로그인 유저 정보가 없습니다."));

        member.updateMember(memberUpdateDto, passwordEncoder);
       return MemberResponseDto.toDto(member);
    }
}
