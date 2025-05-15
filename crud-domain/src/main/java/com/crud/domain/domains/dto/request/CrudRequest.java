/**
 * packageName  : com.crud.domain.domains.dto.request
 * fileName     : CrudRequest
 * author       : SangHoon
 * date         : 2025-01-07
 * description  : 메뉴1 > 메뉴2 작업용 InDto
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-07          SangHoon             최초 생성
 */
package com.crud.domain.domains.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/*@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder*/
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class CrudRequest {

    /** 제목 */
    @NotBlank(message = "제목" + "{valid.not-blank}")
    @Size(min = 1, max = 500, message = "제목" + "{valid.between}")
    private String title;

    /** 내용 */
    @NotBlank(message = "내용" + "{valid.not-blank}")
    @Size(min = 1, message = "내용" + "{valid.min}")
    private String contents;

}