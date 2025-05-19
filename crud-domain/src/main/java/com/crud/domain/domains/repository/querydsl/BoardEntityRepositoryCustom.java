/**
 * packageName  : com.Board.domain.domains.repository.querydsl
 * fileName     : BoardEntityRepositoryCustom
 * author       : SangHoon
 * date         : 2025-05-17
 * description  : Board 테이블 관련 DB 작업(QueryDSL)
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-17          SangHoon             최초 생성
 */
package com.crud.domain.domains.repository.querydsl;

import com.crud.domain.domains.dto.request.BoardSearchCondition;
import com.crud.domain.domains.dto.response.BoardResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardEntityRepositoryCustom {

    /** 조회(단건) */
    BoardResponse findByBoard(Long id);
    BoardResponse findByBoard(Long id, boolean isOrElseThrow);

    /** 조회(전체) */
    List<BoardResponse> findAllBoard(BoardSearchCondition request);
    List<BoardResponse> findAllBoard(BoardSearchCondition request, boolean isOrElseThrow);

    /** 조회(페이징) */
    Page<BoardResponse> findPageBoard(BoardSearchCondition request, Pageable pageable);
    Page<BoardResponse> findPageBoard(BoardSearchCondition request, Pageable pageable, boolean isOrElseThrow);
    
}
