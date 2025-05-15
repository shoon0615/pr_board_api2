/**
 * packageName  : com.crud.domain.domains.repository.querydsl.Impl
 * fileName     : CrudEntityRepositoryImpl
 * author       : SangHoon
 * date         : 2025-01-09
 * description  : CRUD 테이블 관련 DB 작업 구현체
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-09          SangHoon             최초 생성
 */
package com.crud.domain.domains.repository.querydsl.Impl;

import com.crud.common.enumeration.response.ResponseCode;
import com.crud.common.exception.BusinessException;
import com.crud.domain.common.repository.QueryRepositoryExtension;
import com.crud.domain.domains.dto.request.CrudRequest;
import com.crud.domain.domains.dto.request.CrudSearchCondition;
import com.crud.domain.domains.dto.response.CrudResponse;
import com.crud.domain.domains.entity.CrudEntity;
import com.crud.domain.domains.repository.querydsl.CrudEntityRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.crud.domain.domains.entity.QCrudEntity.crudEntity;

//@Repository
@RequiredArgsConstructor
public class CrudEntityRepositoryImpl implements QueryRepositoryExtension, CrudEntityRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private final EntityManager em;
    private final JdbcTemplate jdbcTemplate;

//    @Value("${spring.jpa.properties.hibernate.jdbc.batch_size}")
    private final int BATCH_SIZE = 30;

    @Override
    public CrudResponse findCrudResponse(final Long id) {
        return this.findCrudResponse(id, false);
    }

    @Override
    public CrudResponse findCrudResponse(Long id, final boolean isOrElseThrow) {
        CrudResponse result = this.selectCrudCondition()
                .where(crudEntity.id.eq(id))
//                .fetchFirst();
                .fetchOne();
        return isOrElseThrow ? Optional.ofNullable(result).orElseThrow(() -> new BusinessException(ResponseCode.ErrorCode.FIND_ERROR)) : result;
    }

    @Override
    public List<CrudResponse> findAllCrudResponse(final CrudSearchCondition request) {
        return this.findAllCrudResponse(request, false);
    }

    @Override
    public List<CrudResponse> findAllCrudResponse(final CrudSearchCondition request, final boolean isOrElseThrow) {
        List<CrudResponse> result = this.selectCrudCondition()
                .where(this.whereCrudCondition(request))
                .fetch();

        if (isOrElseThrow && CollectionUtils.isEmpty(result)) {
            throw new BusinessException(ResponseCode.ErrorCode.FIND_ERROR);
        }

        return result;
    }

    @Override
    public Page<CrudResponse> findPageCrudResponse(final CrudSearchCondition request, final Pageable pageable) {
        return this.findPageCrudResponse(request, pageable, false);
    }

    @Override
    public Page<CrudResponse> findPageCrudResponse(final CrudSearchCondition request, final Pageable pageable, final boolean isOrElseThrow) {
        List<CrudResponse> response = this.selectCrudCondition()
                .where(this.whereCrudCondition(request))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
//                .orderBy(crudEntity.id.desc().nullsLast())
                .fetch();
        JPAQuery<Long> total = this.countCrudCondition()
                .where(this.whereCrudCondition(request));
//        return new PageImpl<>(response, pageable, total);
        Page<CrudResponse> result = PageableExecutionUtils.getPage(response, pageable, total::fetchOne);

        if (isOrElseThrow && result.isEmpty()) {
            throw new BusinessException(ResponseCode.ErrorCode.FIND_ERROR);
        }

        return result;
    }

    /**
     * 메뉴1 > 메뉴2 (Select 조건문)
     * @method       : countCrudCondition
     * @author       : SangHoon
     * @date         : 2025-05-13 PM 4:13
     */
    private JPAQuery<CrudResponse> selectCrudCondition() {
        return jpaQueryFactory
                .select(Projections.constructor(CrudResponse.class
                        , crudEntity.title
                        , crudEntity.contents
                ))
                .from(crudEntity);
    }

    private JPAQuery<Long> countCrudCondition() {
        return jpaQueryFactory
                .select(crudEntity.count())
                .from(crudEntity);
    }

    /**
     * 메뉴1 > 메뉴2 (Where 조건문)
     * @method       : whereCrudCondition
     * @author       : SangHoon
     * @date         : 2025-05-13 PM 4:12
     *
     * @param request title, contents
     * @return BooleanBuilder
     */
    private BooleanBuilder whereCrudCondition(CrudSearchCondition request) {
//        return softDeleteFilter(crudEntity.deleteAt)
        return new BooleanBuilder()
                .and(this.titleEq(request))
                .and(this.contentsEq(request));
    }

    /** 제목 */
    /*private BooleanExpression titleEq(CrudSearchCondition request) {
        return StringUtils.hasText(request.getTitle()) ? crudEntity.title.eq(request.getTitle()) : null;
    }*/
    private BooleanBuilder titleEq(CrudSearchCondition request) {
        return nullSafeBooleanBuilder(() -> crudEntity.title.eq(request.getTitle()));
    }

    /** 내용 */
    private BooleanBuilder contentsEq(CrudSearchCondition request) {
        return nullSafeBooleanBuilder(() -> crudEntity.contents.eq(request.getContents()));
    }

    // 추후 biz(req, res), domain(entity) 분리 시 필요??
    @Deprecated
    public List<CrudEntity> findAllCrud(final List<CrudRequest> request) {
//        List<Long> ids = request.stream().map(CrudRequest::getId).toList();
        List<Long> ids = List.of();
        return jpaQueryFactory
                .selectFrom(crudEntity)
//                .join().fetchJoin()
//                .leftJoin().fetchJoin()
                .where(crudEntity.id.in(ids))
                .fetch();
    }

    /** 입력(bulk 연산) */
    @Override
    public void insertAllCrud(final List<CrudEntity> list) {
//        for (CrudEntity entity : list) {
        for (int i = 0; i < list.size(); i++) {
            em.persist(list.get(i));

            // BATCH 단위마다 flush/clear → 메모리 효율 + DB 반영
            if (i % BATCH_SIZE == 0) {
                em.flush();
                em.clear();
            }
        }

        // 마지막 남은 데이터 처리
        em.flush();
        em.clear();
    }

    public long insertAllCrudBatch(final List<CrudRequest> list) {
        String sql = """
                INSERT INTO crud_entity (title, contents)
                VALUES (?, ?)
        """;
//        em.createQuery(sql);

        int[][] batchSize = jdbcTemplate.batchUpdate(sql,
                list,
//                BATCH_SIZE,
                list.size(),    // 자동 분할 개수 -> 대량에서는 고정값을 기준으로 분할하기도 함
                (ps, request) -> {
                    ps.setString(1, request.getTitle());
                    ps.setString(2, request.getContents());
                });

        long insertCnt = Arrays.stream(batchSize)
                .flatMapToInt(Arrays::stream)
//                .sum();                   // 실행 건수 반환
                .filter(cnt -> cnt > 0) // 성공한 건수만 반환
                .count();
        return insertCnt;
    }

    /** 수정(bulk 연산) */
    @Override
//    public void updateAllCrud(final List<CrudEntity> list) {      // 추후 domain(entity), biz(req, res) 구현 시, req 를 제거해야 할 수도 있으니 고려
    public void updateAllCrud(final List<CrudRequest> list) {
        for (int i = 0; i < list.size(); i++) {
//            CrudEntity entity = em.find(CrudEntity.class, list.get(i).getId());
            CrudEntity entity = em.find(CrudEntity.class, i);
            entity.update(list.get(i));

            if (i % BATCH_SIZE == 0) {
                em.flush();
                em.clear();
            }
        }

        em.flush();
        em.clear();
    }

    /** 삭제(bulk 연산) */
    @Override
    public void deleteAllCrud(final List<CrudEntity> list) {
        for (int i = 0; i < list.size(); i++) {
//            CrudEntity entity = em.find(CrudEntity.class, list.get(i).getId());
//            CrudEntity entity = list.get(i).toEntity();
            CrudEntity entity = list.get(i);

            if (!em.contains(entity)) {
                entity = em.merge(entity);
            }
            em.remove(entity);

            if (i % BATCH_SIZE == 0) {
                em.flush();
                em.clear();
            }
        }

        em.flush();
        em.clear();
    }

    /** 삭제(bulk 연산) */
    public long deleteAllCrudBatch() {
        long deleteCnt = jpaQueryFactory
                .delete(crudEntity)
//                .where()
                .execute();
        em.flush();
        em.clear();
        return deleteCnt;
    }

}
