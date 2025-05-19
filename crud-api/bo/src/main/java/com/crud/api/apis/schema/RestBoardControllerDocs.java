/**
 * packageName  : com.crud.api.apis.schema
 * fileName     : RestBoardControllerDocs
 * author       : SangHoon
 * date         : 2025-05-17
 * description  : 게시판 RestController 문서
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-17          SangHoon             최초 생성
 */
package com.crud.api.apis.schema;

import com.crud.domain.domains.dto.request.BoardRequest;
import com.crud.domain.domains.dto.request.BoardSearchCondition;
import org.springframework.http.ResponseEntity;

public interface RestBoardControllerDocs {

    ResponseEntity<?> findByBoard(long id);

    ResponseEntity<?> findPageBoard(BoardSearchCondition request);

    ResponseEntity<?> insertBoard(BoardRequest request);

    ResponseEntity<?> updateBoard(long id, BoardRequest request);

    ResponseEntity<?> deleteBoard(long id);

    /** --- */

    ResponseEntity<?> insertBoardLike(long id);

    ResponseEntity<?> deleteBoardLike(long id);

    ResponseEntity<?> findAllBoardReview(long id);

    ResponseEntity<?> insertBoardReview(long id, BoardRequest.BoardReviewRequest request);

    ResponseEntity<?> updateBoardReview(long reviewId, BoardRequest.BoardReviewRequest request);

    ResponseEntity<?> deleteBoardReview(long reviewId);

}
