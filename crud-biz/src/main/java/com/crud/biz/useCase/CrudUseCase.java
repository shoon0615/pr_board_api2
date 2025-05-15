/**
 * packageName  : com.crud.biz.useCase
 * fileName     : CrudUseCase
 * author       : SangHoon
 * date         : 2025-01-07
 * description  : 메뉴1 > 메뉴2 Service
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-07          SangHoon             최초 생성
 */
package com.crud.biz.useCase;

import com.crud.domain.common.dto.response.PageResponse;
import com.crud.domain.domains.dto.request.CrudRequest;
import com.crud.domain.domains.dto.request.CrudSearchCondition;
import com.crud.domain.domains.dto.response.CrudResponse;

import java.util.List;

//public sealed interface CrudUseCase permits CrudService {
public interface CrudUseCase {

    /** 조회(단건) */
    CrudResponse findByCrud(Long id);

    /** 조회(전체) */
    List<CrudResponse> findAllCrud(CrudSearchCondition request);

    /** 조회(페이징) */
    PageResponse<CrudResponse> findPageCrud(CrudSearchCondition request);

    /** 저장(입력/수정) */
//    void saveCrud(CrudRequest request);

    /** 입력(단일) */
    void insertCrud(CrudRequest request);

    /** 수정(단일) */
    void updateCrud(Long id, CrudRequest request);

    /** 삭제(단일) */
    void deleteCrud(Long id);

    /** 입력(전체) */
    void insertAllCrud(List<CrudRequest> request);

    /** 수정(전체) */
    void updateAllCrud(List<CrudRequest> request);

    /** 삭제(전체) */
    void deleteAllCrud(List<Long> ids);

}
