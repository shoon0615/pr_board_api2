/**
 * packageName  : com.Board.biz.useCase
 * fileName     : BoardUseCase
 * author       : SangHoon
 * date         : 2025-05-17
 * description  : 게시판 Service
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-17          SangHoon             최초 생성
 */
package com.crud.biz.useCase;

import com.crud.domain.common.dto.response.PageResponse;
import com.crud.domain.domains.dto.request.BoardRequest;
import com.crud.domain.domains.dto.request.BoardSearchCondition;
import com.crud.domain.domains.dto.response.BoardResponse;

import java.util.List;

public interface BoardUseCase {

    /** 조회(단건) */
    BoardResponse findByBoard(Long id);

    /** 조회(전체) */
//    List<BoardResponse> findAllBoard(BoardSearchCondition request);

    /** 조회(페이징) */
    PageResponse<BoardResponse> findPageBoard(BoardSearchCondition request);

    /** 입력 */
    void insertBoard(BoardRequest request);

    /** 수정 */
    void updateBoard(Long id, BoardRequest request);

    /** 삭제 */
    void deleteBoard(Long id);

    /** 좋아요 표시 */
    void insertBoardLike(Long id);

    /** 좋아요 해제 */
    void deleteBoardLike(Long id);

    /** 조회(전체) */
    List<BoardResponse.BoardReviewResponse> findAllBoardReview(Long id);

    /** 조회(페이징) */
//    PageResponse<BoardResponse.BoardReviewResponse> findPageBoardReview(BoardRequest.BoardReviewRequest request);

    /** 입력 */
    void insertBoardReview(Long id, BoardRequest.BoardReviewRequest request);

    /** 수정 */
    void updateBoardReview(Long reviewId, BoardRequest.BoardReviewRequest request);

    /** 삭제 */
    void deleteBoardReview(Long reviewId);
    
}
