/**
 * packageName  : com.crud.api.apis.schema
 * fileName     : RestCrudControllerDocs
 * author       : SangHoon
 * date         : 2025-01-10
 * description  : 메뉴1 > 메뉴2 RestController 문서
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-10          SangHoon             최초 생성
 */
package com.crud.api.apis.schema;

import com.crud.api.common.enumeration.SwaggerDictionary;
import com.crud.common.annotation.ApiErrorCodes;
import com.crud.common.annotation.ApiSuccessCode;
import com.crud.domain.domains.dto.request.CrudRequest;
import com.crud.domain.domains.dto.request.CrudSearchCondition;
import com.crud.domain.domains.dto.response.CrudResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import static com.crud.common.enumeration.response.ResponseCode.ErrorCode;
import static com.crud.common.enumeration.response.ResponseCode.SuccessCode;
//import static com.crud.common.enumeration.response.ResponseCode.*;

@Tag(name = "CRUD Controller", description = "메뉴1 > 메뉴2 RestController")
public interface RestCrudControllerDocs {

    @Operation(summary = SwaggerDictionary.FIND, description = SwaggerDictionary.FIND + " 입니다")
    @ApiResponse(
            responseCode = SwaggerDictionary.SUCCESS_CODE, description = SwaggerDictionary.SUCCESS,
            content = @Content(schema = @Schema(implementation = CrudResponse.class))
    )
    @ApiErrorCodes({ ErrorCode.NOT_FOUND, ErrorCode.FIND_ERROR })
    ResponseEntity<?> findByCrud(
            @Parameter(
                    name = "crudId",
                    description = "조회 id",
                    required = true,
                    in = ParameterIn.PATH
            ) long id
    );

    @Operation(summary = SwaggerDictionary.FIND_ALL, description = SwaggerDictionary.FIND_ALL + " 입니다")
    @ApiResponse(
            responseCode = SwaggerDictionary.SUCCESS_CODE, description = SwaggerDictionary.SUCCESS,
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CrudResponse.class)))
    )
    @ApiErrorCodes({ ErrorCode.NOT_FOUND, ErrorCode.FIND_ERROR })
    ResponseEntity<?> findAllCrud(CrudSearchCondition request);

    @Operation(summary = SwaggerDictionary.FIND_PAGE, description = SwaggerDictionary.FIND_PAGE + " 입니다")
    @ApiResponse(
            responseCode = SwaggerDictionary.SUCCESS_CODE, description = SwaggerDictionary.SUCCESS,
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = CrudResponse.class)))
    )
    @ApiErrorCodes({ ErrorCode.NOT_FOUND, ErrorCode.FIND_ERROR })
    ResponseEntity<?> findPageCrud(CrudSearchCondition request);

    @Operation(summary = SwaggerDictionary.INSERT, description = SwaggerDictionary.INSERT + " 입니다")
    @ApiSuccessCode(SuccessCode.OK)
    @ApiErrorCodes({ ErrorCode.NOT_VALID, ErrorCode.NOT_FOUND, ErrorCode.SAVE_ERROR })
    ResponseEntity<?> insertCrud(
            @RequestBody(
                    description = "Crud Request Body",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CrudRequest.class))
            ) CrudRequest request
    );

    @Operation(summary = SwaggerDictionary.UPDATE, description = SwaggerDictionary.UPDATE + " 입니다")
    @ApiSuccessCode(SuccessCode.OK)
    @ApiErrorCodes({ ErrorCode.NOT_VALID, ErrorCode.NOT_FOUND, ErrorCode.SAVE_ERROR })
    ResponseEntity<?> updateCrud(
            @Parameter(
                    name = "crudId",
                    description = "수정 id",
                    required = true,
                    in = ParameterIn.PATH
            ) long id,
            @RequestBody(
                    description = "Crud Request Body",
                    required = true,
                    content = @Content(schema = @Schema(implementation = CrudRequest.class))
            ) CrudRequest request
    );

    @Operation(summary = SwaggerDictionary.DELETE, description = SwaggerDictionary.DELETE + " 입니다")
    @ApiSuccessCode(SuccessCode.OK)
    @ApiErrorCodes({ ErrorCode.NOT_FOUND, ErrorCode.DELETE_ERROR })
    ResponseEntity<?> deleteCrud(
            @Parameter(
                    name = "crudId",
                    description = "삭제 id",
                    required = true,
                    in = ParameterIn.PATH
            ) long id
    );

}
