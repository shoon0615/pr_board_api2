/**
 * packageName  : com.crud.domain.domains.dto.request
 * fileName     : CrudSearchCondition
 * author       : SangHoon
 * date         : 2025-05-12
 * description  : 메뉴1 > 메뉴2 조회용 InDto
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-12          SangHoon             최초 생성
 */
package com.crud.domain.domains.dto.request;

import com.crud.domain.common.dto.request.PageCondition;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/*@Slf4j
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)*/
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class CrudSearchCondition extends PageCondition {

    /** 제목 */
    private String title;

    /** 내용 */
    private String contents;

}
