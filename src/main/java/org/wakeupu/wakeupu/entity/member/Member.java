package org.wakeupu.wakeupu.entity.member;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.wakeupu.wakeupu.dto.member.MemberUpdateDto;
import org.wakeupu.wakeupu.entity.member.Authority;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;
    private String password;
    private String name;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public Member(String email, String password, String name, Authority authority) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.authority = authority;
    }


    public void updateMember(MemberUpdateDto memberUpdateDto, PasswordEncoder passwordEncoder) {
        if (memberUpdateDto.getPassword() != null) this.password = passwordEncoder.encode(memberUpdateDto.getPassword());
        if (memberUpdateDto.getName() != null) this.name = memberUpdateDto.getName();
    }
}