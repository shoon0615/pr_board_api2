/**
 * packageName  : com.crud.domain.domains.dto.request
 * fileName     : LoginRequest
 * author       : SangHoon
 * date         : 2025-04-05
 * description  : 로그인 InDto
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-04-05          SangHoon             최초 생성
 */
package com.crud.domain.domains.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class LoginRequest {

    /** ID */
    @NotBlank(message = "ID" + "{valid.not-blank}")
    private String id;

    /** 비밀번호 */
    @NotBlank(message = "비밀번호" + "{valid.not-blank}")
    private String password;

}