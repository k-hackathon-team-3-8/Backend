package org.wakeupu.wakeupu.dto.sign;

import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.wakeupu.wakeupu.entity.member.Authority;
import org.wakeupu.wakeupu.entity.member.Member;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SignupRequestDto {

    @Email(message = "유효한 이메일을 입력해주세요.")
    @NotBlank(message = "이메일을 입력해주세요.")
    private String email;

    @NotBlank(message = "패스워드를 입력해주세요.")
    private String password;

    @NotBlank(message = "사용자 이름을 입력해주세요.")
    private String name;

    public Member toMember(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .name(name)
                .authority(Authority.ROLE_USER)
                .build();
    }

}
