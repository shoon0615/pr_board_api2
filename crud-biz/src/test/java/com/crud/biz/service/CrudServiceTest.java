/**
 * packageName  : com.crud.biz.service
 * fileName     : CrudServiceTest
 * author       : SangHoon
 * date         : 2025-05-14
 * description  :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-14          SangHoon             최초 생성
 */
package com.crud.biz.service;

import com.crud.common.util.ObjectMapperUtil;
import com.crud.domain.domains.dto.request.CrudRequest;
import com.crud.domain.domains.dto.request.CrudSearchCondition;
import com.crud.domain.domains.dto.response.CrudResponse;
import com.crud.domain.domains.entity.CrudEntity;
import com.crud.domain.domains.repository.CrudEntityRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class CrudServiceTest {

    @Mock
    private CrudEntityRepository crudRepository;

    @InjectMocks
    private CrudService crudService;

    private CrudRequest request;

    @BeforeEach
    void setUp() {
        request = CrudRequest.builder().title("테스트_제목").contents("테스트_내용").build();

        CrudEntity entity = ObjectMapperUtil.convertType(request, CrudEntity.class);

        when(crudRepository.save(any(CrudEntity.class))).thenReturn(entity);
    }

    @DisplayName("입력")
    @Test
    void insertCrud() {
        crudService.insertCrud(request);
        verify(crudRepository).save(any(CrudEntity.class));
    }

}