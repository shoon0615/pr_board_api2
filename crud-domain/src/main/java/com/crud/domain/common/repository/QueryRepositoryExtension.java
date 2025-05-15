/**
 * packageName  : com.crud.domain.common.repository
 * fileName     : QueryRepositoryExtension
 * author       : SangHoon
 * date         : 2025-05-13
 * description  : QueryDSL 사용 시 추가 기능 제공
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-13          SangHoon             최초 생성
 */
package com.crud.domain.common.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.DateTimePath;
import org.springframework.data.repository.NoRepositoryBean;

import java.time.LocalDateTime;
import java.util.function.Supplier;

@NoRepositoryBean
public interface QueryRepositoryExtension {

    /**
     * <pre>
     * 인자의 null 체크(=StringUtils.hasText 역할로 수행)
     * Supplier 를 사용해야 예외가 발생해도 안전하게 처리 가능 (null-safe) -> 지연 평가(LAZY)
     * 1. BooleanExpression: eq(null) 시점에 즉시 발생 -> 예외가 메서드 밖에서 발생하여 try-catch 불가능
     * 2. Supplier<BooleanExpression>: get() 시점에 확인 -> 예외가 메서드 안에서 발생하여 try-catch 가능
     * </pre>
     */
    default BooleanBuilder nullSafeBooleanBuilder(Supplier<BooleanExpression> supplier) {
        return this.convertBooleanBuilder(supplier);
    }

    /** 인자의 deleteAt 체크(null 만 허용) */
    /*default BooleanBuilder softDeleteFilter(BooleanPath deleteYnPath) {
        return this.convertBooleanBuilder(() -> deleteYnPath.eq(false));
    }*/
    default BooleanBuilder softDeleteFilter(DateTimePath<LocalDateTime> deleteAtPath) {
        return this.convertBooleanBuilder(deleteAtPath::isNull);
    }

    /** null-safe 체크 */
    private BooleanBuilder convertBooleanBuilder(Supplier<BooleanExpression> supplier) {
        try {
            return new BooleanBuilder(supplier.get());
        } catch (IllegalArgumentException e) {
            return new BooleanBuilder();
        }
    }

}
