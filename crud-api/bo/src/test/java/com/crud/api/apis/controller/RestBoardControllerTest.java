/**
 * packageName  : com.crud.api.apis.controller
 * fileName     : RestBoardControllerTest
 * author       : SangHoon
 * date         : 2025-05-17
 * description  :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-17          SangHoon             최초 생성
 */
package com.crud.api.apis.controller;

import com.crud.api.config.security.SpringSecurityConfigTest;
import com.crud.biz.service.BoardService;
import com.crud.common.enumeration.response.ResponseCode;
import com.crud.common.util.ObjectMapperUtil;
import com.crud.domain.domains.dto.request.BoardRequest;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RestBoardController.class)
@Import(SpringSecurityConfigTest.class)
//@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class RestBoardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private BoardService boardService;

    private BoardRequest request;

    @BeforeEach
    void setUp() {
        /*request = BoardRequest.builder()
                .build();*/
        request = BoardRequest.builder()
                .title("테스트_제목")
                .contents("테스트_내용")
                .tags(List.of("테스트_태그"))
                .build();
    }

    @DisplayName("조회(단건)")
    @ParameterizedTest
    @ValueSource(longs = {2})
    void findByBoard(Long id) throws Exception {
        mockMvc.perform(get("/api/v1/board/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", notNullValue()))
                .andDo(print())
        ;
    }

    @Test
    void insertBoard() throws Exception {
        mockMvc.perform(post("/api/v1/board")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(ObjectMapperUtil.serialization(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value(ResponseCode.SuccessCode.OK.getMessage()))
        ;
    }

}