/**
 * packageName  : com.crud.api.apis.controller
 * fileName     : RestCrudControllerTest
 * author       : SangHoon
 * date         : 2025-05-13
 * description  :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-13          SangHoon             최초 생성
 */
package com.crud.api.apis.controller;

import com.crud.api.config.security.SpringSecurityConfigTest;
import com.crud.biz.service.CrudService;
import com.crud.common.util.ObjectMapperUtil;
import com.crud.common.util.TddUtil;
import com.crud.domain.domains.dto.request.CrudRequest;
import com.crud.domain.domains.dto.request.CrudSearchCondition;
import com.crud.domain.domains.dto.response.CrudResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@AutoConfigureMockMvc(addFilters = false)   // 테스트용 Security 설정 비활성화
@WebMvcTest(RestCrudController.class)
@Import(SpringSecurityConfigTest.class)     // @WebMvcTest 는 SecurityConfig 를 가져오지 않기에 명시적 설정
class RestCrudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CrudService crudService;

    private CrudSearchCondition request;
    private CrudRequest request2;

    @BeforeEach
    void setUp() {
        request = CrudSearchCondition.builder().title("제목").contents("내용").build();
        request2 = CrudRequest.builder().title("테스트_제목").contents("테스트_내용").build();

        CrudResponse response = ObjectMapperUtil.convertType(request, new TypeReference<>() {});

        when(crudService.findByCrud(any())).thenReturn(response);
        when(crudService.findAllCrud(any(CrudSearchCondition.class))).thenReturn(List.of(response));
//        when(crudService.findPageCrud(any())).thenReturn(PageResponse.from(response));
    }

    @DisplayName("조회(단건)")
    @ParameterizedTest
    @ValueSource(longs = {2})
//    @WithMockUser(username = "test2", password = "12345678", roles = "ADMIN")                   // 정적 주입(실제 x)
    void findByCrud(Long id) throws Exception {
        mockMvc.perform(get("/api/v1/crud/{id}", id)
//                        .with(user("test2").roles(MemberRole.ROLE_ADMIN.getRole()))     // 동적 주입(실제 x)
                        /*.contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtil.serialization(request2))*/              // get 은 content 미사용
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title", is(request.getTitle())))
                .andExpect(jsonPath("$.data.contents", is(request.getContents())))
                .andDo(print())     // 성공해도 테스트 내역 조회
        ;
    }

    @DisplayName("조회(전체)")
    @Test
    void findAllCrud() throws Exception {
        String path = "/api/v1/crud/all";
        String url = TddUtil.convertQueryString(path, request);
        MultiValueMap<String, String> params = TddUtil.convertType(request);

//        mockMvc.perform(get(url))
        mockMvc.perform(get(path).queryParams(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].title", is(request.getTitle())))
                .andExpect(jsonPath("$.data[0].contents", is(request.getContents())))
                .andDo(print())
        ;
    }

    /*@Test
    void findPageCrud() {
    }*/

    @DisplayName("입력")
    @Test
    void insertCrud() throws Exception {
        mockMvc.perform(post("/api/v1/crud")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtil.serialization(request2)))
                .andExpect(status().isOk())
                .andDo(print())
        ;
    }

    @DisplayName("수정")
    @ParameterizedTest
    @ValueSource(longs = {2})
    void updateCrud(Long id) throws Exception {
        mockMvc.perform(put("/api/v1/crud/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtil.serialization(request2)))
                .andExpect(status().isOk())
        ;
    }

    @DisplayName("삭제")
    @ParameterizedTest
    @ValueSource(longs = {2})
    void deleteCrud(Long id) throws Exception {
        mockMvc.perform(delete("/api/v1/crud/{id}", id))
                .andExpect(status().isOk())
        ;
    }
}