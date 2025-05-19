/**
 * packageName  : com.crud.biz.service
 * fileName     : BoardService
 * author       : SangHoon
 * date         : 2025-05-17
 * description  : 게시판 ServiceImpl(구현체)
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-17          SangHoon             최초 생성
 */
package com.crud.biz.service;

import com.crud.biz.useCase.BoardUseCase;
import com.crud.common.enumeration.response.ResponseCode;
import com.crud.common.exception.BusinessException;
import com.crud.common.util.ObjectMapperUtil;
import com.crud.domain.common.dto.response.PageResponse;
import com.crud.domain.domains.dto.request.BoardRequest;
import com.crud.domain.domains.dto.request.BoardSearchCondition;
import com.crud.domain.domains.dto.response.BoardResponse;
import com.crud.domain.domains.entity.board.BoardEntity;
import com.crud.domain.domains.entity.board.BoardLikeEntity;
import com.crud.domain.domains.entity.board.BoardReviewEntity;
import com.crud.domain.domains.entity.board.BoardTagEntity;
import com.crud.domain.domains.entity.member.MemberEntity;
import com.crud.domain.domains.repository.BoardEntityRepository;
import com.crud.domain.domains.repository.BoardLikeEntityRepository;
import com.crud.domain.domains.repository.BoardReviewEntityRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService implements BoardUseCase {

    private final BoardEntityRepository boardRepository;
    private final BoardLikeEntityRepository boardLikeRepository;
    private final BoardReviewEntityRepository boardReviewRepository;

    @Override
    public BoardResponse findByBoard(final Long id) {
        return boardRepository.findByBoard(id);
    }

    @Override
    public PageResponse<BoardResponse> findPageBoard(final BoardSearchCondition request) {
//        Pageable pageable = PageRequest.of(request.getPage()-1, request.getSize(), Sort.by(Sort.Direction.DESC, "id"));
        Pageable pageable = PageRequest.ofSize(10);
        Page<BoardResponse> response = boardRepository.findPageBoard(request, pageable);
        return PageResponse.from(response);
    }

    @Override
    @Transactional
    public void insertBoard(final BoardRequest request) {
        try {
            BoardEntity entity = ObjectMapperUtil.convertType(request, new TypeReference<>() {});

            // (임시) 태그 저장 -> 추후 보완
            if (request.getTags() != null) {
                List<Map<String, String>> tagRequest = request.getTags().stream().map(tag -> Map.of("contents", tag)).toList();
                List<BoardTagEntity> tagEntity = ObjectMapperUtil.convertType(tagRequest, new TypeReference<>() {});
                entity.addTags(tagEntity);
            }

            // TODO: (임시) 회원 하드코딩 -> 추후 삭제
            MemberEntity member = ObjectMapperUtil.convertType(Map.of("id", "test"), new TypeReference<>() {});
            entity.addMember(member);

            boardRepository.save(entity);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.ErrorCode.SAVE_ERROR);
        }
    }

    @Override
    @Transactional
    public void updateBoard(final Long id, final BoardRequest request) {
        try {
            BoardEntity entity = boardRepository.findByIdOrElseThrow(id);

            // (임시) 태그 저장 -> 추후 보완
            if (request.getTags() != null) {
                List<Map<String, String>> tagRequest = request.getTags().stream().map(tag -> Map.of("contents", tag)).toList();
                List<BoardTagEntity> tagEntity = ObjectMapperUtil.convertType(tagRequest, new TypeReference<>() {});
                entity.addTags(tagEntity);
            }

            entity.update(request);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.ErrorCode.SAVE_ERROR);
        }
    }

    @Override
    @Transactional
    public void deleteBoard(final Long id) {
        try {
            boardRepository.deleteById(id);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.ErrorCode.SAVE_ERROR);
        }
    }

    @Override
    @Transactional
    public void insertBoardLike(Long id) {
        try {
            String loginId = "test";

            /** 이미 좋아요 표시를 한 경우, 실패 처리 -> 1~4번 중 선택(=모두 같은 의도) */
            /*// 1-1. 게시판의 좋아요 개수로 확인 -> 추후 사용 시엔 fetch join 필수!
            BoardEntity board = boardRepository.findByIdOrElseThrow(id);
            List<BoardLikeEntity> likes = board.getLike().stream().filter(like -> like.getMember().getId().equals(loginId)).toList();
            if (!CollectionUtils.isEmpty(likes)) {
                throw new BusinessException(ResponseCode.ErrorCode.SAVE_ERROR);
            }

            // 1-2. 저장 로직?? 원래 이렇게 하나??
            MemberEntity member = memberRepository.findByIdOrElseThrow(loginId);
            BoardLikeEntity entity = BoardLikeEntity.fromDto(board, member);    // new BoardLikeEntity(board, member)
            boardLikeRepository.save(entity);*/

            // 2. 게시판에서 좋아요 표시 여부 확인(Entity)
//            BoardEntity board = boardRepository
//                    .findById_BoardLikeAndMember_Id(id, loginId)
//                    .orElseThrow(() -> new BusinessException(ResponseCode.ErrorCode.SAVE_ERROR));
            /*BoardEntity board = boardRepository.findById_BoardLikeAndMember_Id(id, loginId).orElse(null);
            if (board != null) {
                throw new BusinessException(ResponseCode.ErrorCode.SAVE_ERROR);
            }

            // 3. 좋아요에서 좋아요 표시 여부 확인(Entity)
            BoardLikeEntity boardLike = boardLikeRepository.findByBoardIdAndMemberId(id, loginId).orElse(null);
            if (boardLike != null) {
                throw new BusinessException(ResponseCode.ErrorCode.SAVE_ERROR);
            }

            // 4. 좋아요에서 좋아요 표시 여부 확인(boolean)
            if (boardLikeRepository.existsByBoardIdAndMemberId(id, loginId)) {
                throw new BusinessException(ResponseCode.ErrorCode.SAVE_ERROR);
            }*/

            BoardEntity board = boardRepository.findByIdOrElseThrow(id);
            if (boardLikeRepository.existsByBoardIdAndMemberId(id, loginId)) {
                throw new BusinessException(ResponseCode.ErrorCode.SAVE_ERROR);
            }

            // (임시) 추후 보완 -> 현재: @MapsId 를 이용한 save 처리
//            Map<String, Long> request = Map.of(
            Map<String, Object> request = Map.of(
                    "boardId", id,
                    "memberId", loginId
            );
            BoardLikeEntity entity = ObjectMapperUtil.convertType(request, new TypeReference<>() {});
            boardLikeRepository.save(entity);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.ErrorCode.SAVE_ERROR);
        }
    }

    @Override
    @Transactional
    public void deleteBoardLike(Long id) {
        try {
            String loginId = "test";
            BoardLikeEntity boardLike = boardLikeRepository
                    .findByBoardIdAndMemberId(id, loginId)
                    .orElseThrow(() -> new BusinessException(ResponseCode.ErrorCode.DELETE_ERROR));
            boardLikeRepository.delete(boardLike);

            // deleteById 이용 방법 -> select 후 delete 진행되어 비효율적
//            boardLikeRepository.deleteById(boardLike.getId());

            // 고아 객체 제거 방법(orphanRemoval = true) -> 여기 상황에선 비효율적
//            BoardEntity board = boardRepository.findByIdOrElseThrow(id);
//            board.getLike().remove(boardLike);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.ErrorCode.SAVE_ERROR);
        }
    }

    @Override
    public List<BoardResponse.BoardReviewResponse> findAllBoardReview(Long id) {
        // ❌ 1. board 에서 review 를 찾는 경우
//        BoardEntity entity = boardRepository.findByIdOrElseThrow(id);
//        List<BoardReviewEntity> review = entity.getReview();      // N+1 발생

        /*
            ❌ 반면 잘못된 방식 (비추천)
            ✅ 가능은 하지만 → 단일 게시글 + 댓글 전체 조회가 필요할 때만 사용
            ❌ 댓글만 필요한 상황에서는 카티시안 곱, 불필요한 데이터 로딩, paging 불가능
         */
//        @Query("SELECT b FROM BoardEntity b JOIN FETCH b.review WHERE b.id = :id")
//        List<BoardEntity> findById_Review(@Param("id") Long id);


        // ✅ 2. review 에서 board_id 를 찾는 경우
        List<BoardReviewEntity> entity = boardReviewRepository.findByBoardId(id);
//        List<BoardResponse.BoardReviewResponse> result = ObjectMapperUtil.convertType(entity, new TypeReference<>() {});  // LAZY 객체로 인해 에러 발생
//        List<BoardResponse.BoardReviewResponse> result = entity.stream().map(BoardResponse.BoardReviewResponse::fromEntity).toList();
//        List<BoardResponse.BoardReviewResponse> result = entity.stream().map(BoardReviewEntity::toDto).toList();
        List<Map<String, Object>> reviews = entity.stream().map(review -> {
            Map<String, Object> result = new HashMap<>();
            result.put("reviewId", review.getId());     // getId 가 long 이 아닌 Long 이라 null 가능성으로 불변성 객체로 생성 불가(=Map.of)
            result.put("contents", review.getContents());
            return result;
        }).toList();
        List<BoardResponse.BoardReviewResponse> result = ObjectMapperUtil.convertType(reviews, new TypeReference<>() {});
        return result;
    }

    @Override
    @Transactional
    public void insertBoardReview(Long id, BoardRequest.BoardReviewRequest request) {
        try {
            String loginId = "test";

            BoardEntity board = boardRepository.findByIdOrElseThrow(id);

            // TODO: 저장 로직?? 원래 이렇게 하나?? 아니면 메인 + 연관관계로 한번에 저장(persist) 안해서?? 연관관계 적용 안해야 되나??
            /*MemberEntity member = memberRepository.findByIdOrElseThrow(loginId);
            BoardReviewEntity entity = ObjectMapperUtil.convertType(request, new TypeReference<>() {});
            entity.fromDto(board, member);
            boardReviewRepository.save(entity);*/

            // TODO: (임시) 추후 보완 -> 현재: @MapsId 를 이용한 save 처리
            Map<String, Object> reviewRequest = Map.of(
                    "boardId", id,
                    "memberId", loginId,
                    "contents", request.getContents()
            );
            BoardReviewEntity entity = ObjectMapperUtil.convertType(reviewRequest, new TypeReference<>() {});
            boardReviewRepository.save(entity);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.ErrorCode.SAVE_ERROR);
        }
    }

    @Override
    @Transactional
    public void updateBoardReview(Long reviewId, BoardRequest.BoardReviewRequest request) {
        try {
            BoardReviewEntity entity = boardReviewRepository.findByIdOrElseThrow(reviewId);
            entity.update(request);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.ErrorCode.SAVE_ERROR);
        }
    }

    @Override
    @Transactional
    public void deleteBoardReview(Long reviewId) {
        try {
            boardReviewRepository.deleteById(reviewId);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ResponseCode.ErrorCode.SAVE_ERROR);
        }
    }
}
