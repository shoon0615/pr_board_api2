/**
 * packageName  : com.crud.domain.domains.dto.request
 * fileName     : MemberRequest
 * author       : SangHoon
 * date         : 2025-04-05
 * description  : 회원 InDto
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-04-05          SangHoon             최초 생성
 */
package com.crud.domain.domains.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class MemberRequest {

    /** ID */
    @NotBlank(message = "ID" + "{valid.not-blank}")
    @Pattern(regexp = "[0-9a-z]+", message = "ID" + "{valid.pattern}")
    @Size(min = 4, max = 10, message = "ID" + "{valid.between}")
    private String id;

    /** 비밀번호 */
    @NotBlank(message = "비밀번호" + "{valid.not-blank}")
    @Pattern(regexp = "[0-9a-zA-Z]+", message = "비밀번호" + "{valid.pattern}")
    @Size(min = 8, max = 15, message = "비밀번호" + "{valid.between}")
    private String password;

}