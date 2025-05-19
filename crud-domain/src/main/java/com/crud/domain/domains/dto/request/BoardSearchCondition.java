/**
 * packageName  : com.crud.domain.domains.dto.request
 * fileName     : BoardSearchCondition
 * author       : SangHoon
 * date         : 2025-05-17
 * description  : 게시판 조회용 InDto
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-17          SangHoon             최초 생성
 */
package com.crud.domain.domains.dto.request;

import com.crud.domain.common.dto.request.PageCondition;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class BoardSearchCondition extends PageCondition  {

}
