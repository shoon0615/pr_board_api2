/**
 * packageName  : com.crud.api.config.swagger
 * fileName     : ExampleHolder
 * author       : SangHoon
 * date         : 2025-01-23
 * description  : Swagger 의 @ApiResponse 반환 데이터에 객체 주입
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-23          SangHoon             최초 생성
 */
package com.crud.api.config.swagger;

import io.swagger.v3.oas.models.examples.Example;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExampleHolder {
    private Example holder;
    private String name;
    private int code;
}
