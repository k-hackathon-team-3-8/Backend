package org.wakeupu.wakeupu.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.wakeupu.wakeupu.entity.member.Member;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberResponseDto {

    private String email;
    private String password;
    private String name;

    static public  MemberResponseDto toDto(Member m) {
        return new MemberResponseDto(m.getEmail(), m.getPassword(), m.getName());
    }

}
