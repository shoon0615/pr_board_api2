/**
 * packageName  : com.crud.domain.domains.repository
 * fileName     : CrudEntityRepositoryTest
 * author       : SangHoon
 * date         : 2025-05-12
 * description  :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-12          SangHoon             최초 생성
 */
package com.crud.domain.domains.repository;

import com.crud.common.util.ObjectMapperUtil;
import com.crud.domain.config.jpa.JpaConfigTest;
import com.crud.domain.domains.dto.request.CrudRequest;
import com.crud.domain.domains.entity.CrudEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfigTest.class)
@ContextConfiguration(classes = com.crud.api.CrudApiBoApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class CrudEntityRepositoryTest {

    @Autowired
    private CrudEntityRepository crudRepository;

    private CrudRequest request;

    @BeforeEach
    void setUp() {
        request = CrudRequest.builder().title("테스트_제목").contents("테스트_내용").build();
    }

    @DisplayName("입력")
    @Test
//    @Rollback(false)
    void save() {
        /** given */
//        CrudEntity entity = ObjectMapperUtil.convertType(request, CrudEntity.class);
        CrudEntity entity = ObjectMapperUtil.convertType(request, new TypeReference<>() {});

        /** when */
        CrudEntity result = crudRepository.save(entity);

        /** then */
        Long id = result.getId();
//        CrudEntity response = crudRepository.findByIdOrElseThrow(id);
        CrudEntity response = crudRepository.findByIdOrElseNull(id);

        /*assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(request.getTitle());
        assertThat(response.getContents()).isEqualTo(request.getContents());*/

        /*assertThat(response)
                .extracting("title", "contents")
                .contains(request.getTitle(), request.getContents());*/

        String[] IGNORE_PROPERTIES = {"id", "deletedAt", "createdDate", "modifiedDate", "createdBy", "modifiedBy"};
        assertThat(response)
                .usingRecursiveComparison()
                .ignoringFields(IGNORE_PROPERTIES)
                .ignoringFields("hits")
                .isEqualTo(request);
    }

}