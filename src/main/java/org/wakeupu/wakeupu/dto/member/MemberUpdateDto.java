package org.wakeupu.wakeupu.dto.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MemberUpdateDto {

    private String password;
    private String name;

}
