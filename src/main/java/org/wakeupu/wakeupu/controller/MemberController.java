package org.wakeupu.wakeupu.controller;

import lombok.RequiredArgsConstructor;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wakeupu.wakeupu.dto.member.MemberResponseDto;
import org.wakeupu.wakeupu.dto.member.MemberUpdateDto;
import org.wakeupu.wakeupu.service.MemberService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @GetMapping
    public ResponseEntity<MemberResponseDto> getMyInfo() {
        return ResponseEntity.ok(memberService.getMyInfo());
    }

    @PutMapping()
    public ResponseEntity<MemberResponseDto> updateMyInfo(@Valid @RequestBody MemberUpdateDto memberUpdateDto) {
        memberService.updateMyInfo(memberUpdateDto);
        return ResponseEntity.ok(memberService.getMyInfo());
    }

}
