/**
 * packageName  : com.crud.domain.domains.dto.response
 * fileName     : CrudResponse
 * author       : SangHoon
 * date         : 2025-01-07
 * description  : 메뉴1 > 메뉴2 OutDto
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-07          SangHoon             최초 생성
 */
package com.crud.domain.domains.dto.response;

import lombok.*;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CrudResponse {

    /** 제목 */
    private String title;

    /** 내용 */
    private String contents;

    @Builder
//    @QueryProjection
    public CrudResponse(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

}
