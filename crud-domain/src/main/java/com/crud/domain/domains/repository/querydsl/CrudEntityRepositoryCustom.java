/**
 * packageName  : com.crud.domain.domains.repository.querydsl
 * fileName     : CrudEntityRepositoryCustom
 * author       : SangHoon
 * date         : 2025-01-09
 * description  : CRUD 테이블 관련 DB 작업(QueryDSL)
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-09          SangHoon             최초 생성
 */
package com.crud.domain.domains.repository.querydsl;

import com.crud.domain.domains.dto.request.CrudRequest;
import com.crud.domain.domains.dto.request.CrudSearchCondition;
import com.crud.domain.domains.dto.response.CrudResponse;
import com.crud.domain.domains.entity.CrudEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CrudEntityRepositoryCustom {

    /** 조회(단건) */
    CrudResponse findCrudResponse(Long id);
    CrudResponse findCrudResponse(Long id, boolean isOrElseThrow);

    /** 조회(전체) */
    List<CrudResponse> findAllCrudResponse(CrudSearchCondition request);
    List<CrudResponse> findAllCrudResponse(CrudSearchCondition request, boolean isOrElseThrow);

    /** 조회(페이징) */
    Page<CrudResponse> findPageCrudResponse(CrudSearchCondition request, Pageable pageable);
    Page<CrudResponse> findPageCrudResponse(CrudSearchCondition request, Pageable pageable, boolean isOrElseThrow);

    /** 입력(bulk 연산) */
//    void insertAllCrud(List<CrudRequest> request);
    void insertAllCrud(List<CrudEntity> list);

    /** 수정(bulk 연산) */
//    void updateAllCrud(List<CrudEntity> list);
    void updateAllCrud(List<CrudRequest> list);

    /** 삭제(bulk 연산) */
    void deleteAllCrud(List<CrudEntity> list);

}
