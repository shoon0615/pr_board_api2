/**
 * packageName  : com.crud.domain.domains.repository.querydsl.Impl
 * fileName     : BoardEntityRepositoryImpl
 * author       : SangHoon
 * date         : 2025-05-17
 * description  : Board 테이블 관련 DB 작업 구현체
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-17          SangHoon             최초 생성
 */
package com.crud.domain.domains.repository.querydsl.Impl;

import com.crud.common.enumeration.response.ResponseCode;
import com.crud.common.exception.BusinessException;
import com.crud.domain.common.repository.QueryRepositoryExtension;
import com.crud.domain.domains.dto.request.BoardSearchCondition;
import com.crud.domain.domains.dto.response.BoardResponse;
import com.crud.domain.domains.repository.querydsl.BoardEntityRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.crud.domain.domains.entity.board.QBoardEntity.boardEntity;
import static com.crud.domain.domains.entity.board.QBoardLikeEntity.boardLikeEntity;
import static com.crud.domain.domains.entity.board.QBoardReviewEntity.boardReviewEntity;
import static com.crud.domain.domains.entity.board.QBoardTagEntity.boardTagEntity;
import static com.crud.domain.domains.entity.member.QMemberEntity.memberEntity;

@RequiredArgsConstructor
public class BoardEntityRepositoryImpl implements QueryRepositoryExtension, BoardEntityRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public BoardResponse findByBoard(Long id) {
        return this.findByBoard(id, false);
    }

    /*@Override
    public BoardResponse findByBoard(Long id, boolean isOrElseThrow) {
        BoardResponse result = this.selectBoardCondition()
                .where(boardEntity.id.eq(id))
                .fetchOne();
        return isOrElseThrow ? Optional.ofNullable(result).orElseThrow(() -> new BusinessException(ResponseCode.ErrorCode.FIND_ERROR)) : result;
    }*/

    @Override
    public BoardResponse findByBoard(Long id, boolean isOrElseThrow) {
        JPAQuery<?> apply = this.selectBoardResponseCondition()
                .where(boardEntity.id.eq(id));
        Map<Long, BoardResponse> result = this.selectBoardResponse(apply);
        return isOrElseThrow ? Optional.ofNullable(result.get(id)).orElseThrow(() -> new BusinessException(ResponseCode.ErrorCode.FIND_ERROR)) : result.get(id);
    }

    @Override
    public List<BoardResponse> findAllBoard(BoardSearchCondition request) {
        return this.findAllBoard(request, false);
    }

    @Override
    public List<BoardResponse> findAllBoard(BoardSearchCondition request, boolean isOrElseThrow) {
        JPAQuery<?> apply = this.selectBoardResponseCondition()
                .where(this.whereBoardCondition(request));
        List<BoardResponse> result = List.copyOf(this.selectBoardResponse(apply).values());

        if (isOrElseThrow && CollectionUtils.isEmpty(result)) {
            throw new BusinessException(ResponseCode.ErrorCode.FIND_ERROR);
        }

        return result;
    }

    @Override
    public Page<BoardResponse> findPageBoard(BoardSearchCondition request, Pageable pageable) {
        return this.findPageBoard(request, pageable, false);
    }

    @Override
    public Page<BoardResponse> findPageBoard(BoardSearchCondition request, Pageable pageable, boolean isOrElseThrow) {
        JPAQuery<?> apply = this.selectBoardResponseCondition()
                .where(this.whereBoardCondition(request))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
//                .orderBy(boardEntity.id.desc().nullsLast())
                ;

        /*pageable.getSort().stream().forEach(sort -> {
            Order order = sort.isAscending() ? Order.ASC : Order.DESC;
            String property = sort.getProperty();

            Path<Object> target = Expressions.path(Object.class, boardEntity, property);
            OrderSpecifier<?> orderSpecifier = new OrderSpecifier(order, target);
            apply.orderBy(orderSpecifier);
        });*/

//        List<BoardResponse> response = new ArrayList<>(this.selectBoardResponse(apply).values());     // null 허용
        List<BoardResponse> response = List.copyOf(this.selectBoardResponse(apply).values());         // null 불허 -> 불변

        JPAQuery<Long> total = this.countBoardCondition()
                .where(this.whereBoardCondition(request));

        Page<BoardResponse> result = PageableExecutionUtils.getPage(response, pageable, total::fetchOne);

        if (isOrElseThrow && result.isEmpty()) {
            throw new BusinessException(ResponseCode.ErrorCode.FIND_ERROR);
        }

        return result;
    }

    /**
     * 게시판 Select 조건문
     * @method       : selectBoardCondition
     * @author       : SangHoon
     * @date         : 2025-05-17 PM 11:50
     */
    private JPAQuery<BoardResponse> selectBoardCondition() {
        return jpaQueryFactory
                .select(Projections.constructor(BoardResponse.class
                        , boardEntity.title
                        , boardEntity.contents
                ))
                .from(boardEntity);
    }

    private JPAQuery<Long> countBoardCondition() {
        return jpaQueryFactory
                .select(boardEntity.count())
                .from(boardEntity);
    }

    private JPAQuery<?> selectBoardResponseCondition() {
        return jpaQueryFactory
//                .select(boardEntity)      // 작성 안하면 default 인듯?? -> 기본 board 만 쓰다가 LAZY 로 사용 시, 자동으로 inner join 적용
//                .selectDistinct(boardEntity)
//                .selectDistinct(boardEntity.id, boardEntity.title, boardEntity.contents, boardEntity.hits,
//                        memberEntity.memberInfo.name, boardTagEntity.contents)
                .from(boardEntity)
                .join(boardEntity.member, memberEntity)
//                .leftJoin(boardEntity.like, boardLikeEntity)        // 불필요 -> 작성 시, outer join 으로 인해 카타시안 곱 발생
//                .leftJoin(boardEntity.review, boardReviewEntity)
                .leftJoin(boardEntity.tag, boardTagEntity)          // 미작성 시, inner join 으로 적용
                ;
    }

    private Map<Long, BoardResponse> selectBoardResponse(JPAQuery<?> query) {
        return query
                .transform(GroupBy.groupBy(boardEntity.id).as(Projections.constructor(BoardResponse.class
                        , boardEntity.title
                        , boardEntity.contents
                        , boardEntity.hits
                        , memberEntity.memberInfo.name
                        , Expressions.asNumber(
//                                JPAExpressions.select(Expressions.numberPath(Integer.class, boardLikeEntity, "count"))   // 불가능 -> QClass 에 존재하는 컬럼만 가능
                                JPAExpressions.select(boardLikeEntity.count())
                                        .from(boardLikeEntity)
                                        .where(boardLikeEntity.board.id.eq(boardEntity.id))
//                                        .groupBy(boardLikeEntity.board.id)
                        ).intValue()
                        , GroupBy.list(boardTagEntity.contents)
                        , Expressions.asNumber(
                                JPAExpressions.select(boardReviewEntity.count())
                                        .from(boardReviewEntity)
                                        .where(boardReviewEntity.board.id.eq(boardEntity.id))
                        ).intValue()
                )));
    }

    /**
     * 게시판 Where 조건문
     * @method       : whereBoardCondition
     * @author       : SangHoon
     * @date         : 2025-05-17 PM 11:50
     */
    private BooleanBuilder whereBoardCondition(BoardSearchCondition request) {
        return new BooleanBuilder()
//                .and(this.titleEq(request))
//                .and(this.contentsEq(request))
                ;
    }

    /** 제목 */
    private BooleanBuilder titleEq(BoardSearchCondition request) {
//        return nullSafeBooleanBuilder(() -> boardEntity.title.eq(request.getTitle()));
        return new BooleanBuilder();
    }

    /** 내용 */
    private BooleanBuilder contentsEq(BoardSearchCondition request) {
//        return nullSafeBooleanBuilder(() -> boardEntity.contents.eq(request.getContents()));
        return new BooleanBuilder();
    }

}