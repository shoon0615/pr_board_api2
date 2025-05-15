/**
 * packageName  : com.crud.biz.service
 * fileName     : CrudService
 * author       : SangHoon
 * date         : 2025-01-07
 * description  : 메뉴1 > 메뉴2 ServiceImpl(구현체)
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-07          SangHoon             최초 생성
 */
package com.crud.biz.service;

import com.crud.biz.useCase.CrudUseCase;
import com.crud.common.exception.BusinessException;
import com.crud.common.util.ObjectMapperUtil;
import com.crud.domain.common.dto.response.PageResponse;
import com.crud.domain.domains.dto.request.CrudRequest;
import com.crud.domain.domains.dto.request.CrudSearchCondition;
import com.crud.domain.domains.dto.response.CrudResponse;
import com.crud.domain.domains.entity.CrudEntity;
import com.crud.domain.domains.repository.CrudEntityRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.crud.common.enumeration.response.ResponseCode.ErrorCode;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CrudService implements CrudUseCase {

    private final CrudEntityRepository crudEntityRepository;

    @Override
    public CrudResponse findByCrud(final Long id) {
        return crudEntityRepository.findCrudResponse(id);
    }

    @Override
    public List<CrudResponse> findAllCrud(final CrudSearchCondition request) {
        /*
            [ DTO 변환 ]
            0. JpaRepository 에서 직접 작업
            1. toEntity
            2. 정적 팩토리 메소드
            3. Mapstruct
            4. ObjectMapper(리플렉션으로 인한 성능 저하 이슈) -> 임시 채택
         */
        /*List<CrudEntity> entity = crudEntityRepository.findAll();
        List<CrudResponse> response = ObjectMapperUtil.convertType(entity, new TypeReference<>() {});
        return response*/

        return crudEntityRepository.findAllCrudResponse(request);
    }

    @Override
    public PageResponse<CrudResponse> findPageCrud(final CrudSearchCondition request) {
//        Pageable pageable = PageRequest.of(request.getPage()-1, request.getSize());
        Pageable pageable = PageRequest.ofSize(10);
        Page<CrudResponse> response = crudEntityRepository.findPageCrudResponse(request, pageable);
        return PageResponse.from(response);
    }

    /*@Override
    @Transactional
    public CrudResponse insertCrud(final CrudRequest request) {
        CrudResponse response;
        try {
            CrudEntity entity = ObjectMapperUtil.convertType(request, new TypeReference<>() {});
            CrudEntity result = crudEntityRepository.save(entity);
            response = ObjectMapperUtil.convertType(request, new TypeReference<>() {});
        }
        return response;
    }*/

    @Override
    @Transactional
    public void insertCrud(final CrudRequest request) {
        try {
            CrudEntity entity = ObjectMapperUtil.convertType(request, new TypeReference<>() {});
            CrudEntity result = crudEntityRepository.save(entity);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
    }

    @Override
    @Transactional
    public void updateCrud(final Long id, final CrudRequest request) {
        try {
            CrudEntity entity = crudEntityRepository.findByIdOrElseThrow(id);
            CrudEntity result = entity.update(request);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
    }

    @Override
    @Transactional
    public void deleteCrud(final Long id) {
        try {
            /*crudEntityRepository.findByIdOrElseThrow(id);
            crudEntityRepository.delete(id);*/
            crudEntityRepository.deleteById(id);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.DELETE_ERROR);
        }
    }

    /*@Override
    @Transactional
    public long insertAllCrud(List<CrudRequest> request) {
        long insertCnt;     // default -> Long: null, long: 0
        try {
            List<CrudEntity> entity = ObjectMapperUtil.convertType(request, new TypeReference<>() {});
            List<CrudEntity> result = crudEntityRepository.saveAll(entity);
            insertCnt = result.size();
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
        return insertCnt;
    }*/

    @Override
    @Transactional
    public void insertAllCrud(List<CrudRequest> request) {
        try {
            List<CrudEntity> entity = ObjectMapperUtil.convertType(request, new TypeReference<>() {});
            crudEntityRepository.insertAllCrud(entity);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
    }

    @Override
    @Transactional
    public void updateAllCrud(List<CrudRequest> request) {
        try {
            /*List<Long> ids = request.stream().map(CrudRequest::getId).toList();
            List<CrudEntity> list = crudEntityRepository.findAllById(ids);
//            List<CrudEntity> list = crudEntityRepository.findAllCrud(request);  // fetch join
            crudEntityRepository.updateAllCrud(list);*/
            crudEntityRepository.updateAllCrud(request);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
    }

    @Override
    @Transactional
    public void deleteAllCrud(List<Long> ids) {
        try {
//            List<Long> ids = request.stream().map(CrudRequest::getId).toList();
//            List<CrudEntity> list = crudEntityRepository.findAllCrud(request);  // fetch join
            List<CrudEntity> list = crudEntityRepository.findAllById(ids);
            crudEntityRepository.deleteAllCrud(list);
        } catch (Exception e) {
            throw new BusinessException(ErrorCode.SAVE_ERROR);
        }
    }

}
