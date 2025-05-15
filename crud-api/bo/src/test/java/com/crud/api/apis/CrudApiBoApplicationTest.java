/**
 * packageName  : com.crud.api.apis
 * fileName     : CrudApiBoApplicationTest
 * author       : SangHoon
 * date         : 2025-05-14
 * description  :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-14          SangHoon             최초 생성
 */
package com.crud.api.apis;

import com.crud.biz.service.CrudService;
import com.crud.common.util.TddUtil;
import com.crud.domain.domains.dto.request.CrudSearchCondition;
import com.crud.domain.domains.dto.response.CrudResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)   // 이름 자동 생성
@AutoConfigureMockMvc
@ActiveProfiles("local")
public class CrudApiBoApplicationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CrudService crudService;

    private CrudSearchCondition request;

    @BeforeEach
    void setUp() {
        request = CrudSearchCondition.builder().title("제목3").contents("내용3").build();
    }

    @DisplayName("조회(단건)")
    @ParameterizedTest
    @ValueSource(longs = {2})
    void findByCrud(Long id) throws Exception {
        CrudResponse response = crudService.findByCrud(id);
        assertThat(response).isNotNull();
        assertThat(response.getTitle()).isEqualTo(request.getTitle());
        assertThat(response.getContents()).isEqualTo(request.getContents());

        mockMvc.perform(get("/api/v1/crud/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is(request.getTitle())))
                .andExpect(jsonPath("$.data.contents", is(request.getContents())))
//                .andDo(print())
        ;
    }

    @DisplayName("조회(전체)")
    @Test
    void findAllCrud() throws Exception {
        List<CrudResponse> response = crudService.findAllCrud(request);
        assertThat(response)
                .hasSizeGreaterThan(0)
//                .filteredOn(e -> e.getId() == 1)
                .filteredOn(e -> e.getTitle().equals(request.getTitle()))
                .usingRecursiveComparison()
                .isEqualTo(request);

        String path = "/api/v1/crud/all";
        String url = TddUtil.convertQueryString(path, request);
        MultiValueMap<String, String> params = TddUtil.convertType(request);

//        mockMvc.perform(get(url))
        mockMvc.perform(get(path).queryParams(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$.data", hasSize(equalTo(response.size()))))
//                .andExpect(jsonPath("$.data[0].title", is(request.getTitle())))
//                .andExpect(jsonPath("$.data[0].contents", is(request.getContents())))
                .andDo(print())
        ;
    }

}
