/**
 * packageName  : com.crud.api.apis.controller
 * fileName     : RestBoardController
 * author       : SangHoon
 * date         : 2025-05-17
 * description  : 게시판 RestController
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-17          SangHoon             최초 생성
 */
package com.crud.api.apis.controller;

import com.crud.api.apis.schema.RestBoardControllerDocs;
import com.crud.biz.service.BoardService;
import com.crud.common.dto.response.ApiResponse;
import com.crud.domain.domains.dto.request.BoardRequest;
import com.crud.domain.domains.dto.request.BoardSearchCondition;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/board")
public class RestBoardController implements RestBoardControllerDocs {

    private final BoardService boardService;

    /**
     * 조회(단건)
     * @method       : findByBoard
     * @author       : SangHoon
     * @date         : 2025-05-17 PM 9:21
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> findByBoard(@PathVariable final long id) {
        return ApiResponse.ok(boardService.findByBoard(id));
    }

    /**
     * 조회(페이징)
     * @method       : findPageBoard
     * @author       : SangHoon
     * @date         : 2025-05-17 PM 9:21
     */
    @Override
    @GetMapping
    public ResponseEntity<?> findPageBoard(final BoardSearchCondition request) {
        return ApiResponse.ok(boardService.findPageBoard(request));
    }

    /**
     * 입력
     * @method       : insertBoard
     * @author       : SangHoon
     * @date         : 2025-05-17 PM 9:21
     */
    @Override
    @PostMapping
    public ResponseEntity<?> insertBoard(@Valid @RequestBody final BoardRequest request) {
        boardService.insertBoard(request);
        return ApiResponse.ok();
    }

    /**
     * 수정
     * @method       : updateBoard
     * @author       : SangHoon
     * @date         : 2025-05-17 PM 9:21
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBoard(@PathVariable final long id, @Valid @RequestBody final BoardRequest request) {
        boardService.updateBoard(id, request);
        return ApiResponse.ok();
    }

    /**
     * 삭제
     * @method       : deleteBoard
     * @author       : SangHoon
     * @date         : 2025-05-17 PM 9:21
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable final long id) {
        boardService.deleteBoard(id);
        return ApiResponse.ok();
    }

    /** --- */

    /**
     * 좋아요 표시
     * @method       : insertBoardLike
     * @author       : SangHoon
     * @date         : 2025-05-19 PM 6:32
     */
    @Override
    @PostMapping("/like/{id}")
    public ResponseEntity<?> insertBoardLike(@PathVariable final long id) {
        boardService.insertBoardLike(id);
        return ApiResponse.ok();
    }

    /**
     * 좋아요 해제
     * @method       : insertBoardLike
     * @author       : SangHoon
     * @date         : 2025-05-19 PM 6:32
     */
    @Override
    @DeleteMapping("/like/{id}")
    public ResponseEntity<?> deleteBoardLike(@PathVariable final long id) {
        boardService.deleteBoardLike(id);
        return ApiResponse.ok();
    }

    /**
     * 리뷰 조회(단건)
     * @method       : findByBoardReview
     * @author       : SangHoon
     * @date         : 2025-05-20 AM 1:54
     */
    @Override
    @GetMapping("/review/{id}")
    public ResponseEntity<?> findAllBoardReview(@PathVariable final long id) {
        return ApiResponse.ok(boardService.findAllBoardReview(id));
//        return ApiResponse.ok(boardService.findPageBoardReview(id));
    }

    /**
     * 리뷰 입력 -> 추후 대댓글 기능 필요
     * @method       : insertBoardReview
     * @author       : SangHoon
     * @date         : 2025-05-20 AM 1:54
     */
    @Override
    @PostMapping("/review/{id}")
    public ResponseEntity<?> insertBoardReview(@PathVariable final long id, @Valid @RequestBody final BoardRequest.BoardReviewRequest request) {
        boardService.insertBoardReview(id, request);
        return ApiResponse.ok();
    }

    /**
     * 리뷰 수정
     * @method       : updateBoardReview
     * @author       : SangHoon
     * @date         : 2025-05-20 AM 1:54
     */
    @Override
    @PutMapping("/review/{reviewId}")
    public ResponseEntity<?> updateBoardReview(@PathVariable final long reviewId, @Valid @RequestBody final BoardRequest.BoardReviewRequest request) {
        boardService.updateBoardReview(reviewId, request);
        return ApiResponse.ok();
    }

    /**
     * 리뷰 삭제
     * @method       : deleteBoardReview
     * @author       : SangHoon
     * @date         : 2025-05-20 AM 1:54
     */
    @Override
    @DeleteMapping("/review/{reviewId}")
    public ResponseEntity<?> deleteBoardReview(@PathVariable final long reviewId) {
        boardService.deleteBoardReview(reviewId);
        return ApiResponse.ok();
    }

}