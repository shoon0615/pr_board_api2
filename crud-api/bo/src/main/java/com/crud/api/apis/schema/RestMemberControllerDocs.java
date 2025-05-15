/**
 * packageName  : com.crud.api.apis.schema
 * fileName     : RestMemberControllerDocs
 * author       : SangHoon
 * date         : 2025-04-09
 * description  : 회원 관련 RestController 문서
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-04-09          SangHoon             최초 생성
 */
package com.crud.api.apis.schema;

import com.crud.api.common.enumeration.SwaggerDictionary;
import com.crud.common.annotation.ApiErrorCodes;
import com.crud.common.annotation.ApiSuccessCode;
import com.crud.domain.domains.dto.request.LoginRequest;
import com.crud.domain.domains.dto.request.MemberRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static com.crud.common.enumeration.response.ResponseCode.SuccessCode;
import static com.crud.common.enumeration.response.ResponseCode.ErrorCode;
//import static com.crud.common.enumeration.response.ResponseCode.*;

@Tag(name = "Member Controller", description = "회원 관련 RestController")
public interface RestMemberControllerDocs {

    @Operation(summary = SwaggerDictionary.LOGIN, description = SwaggerDictionary.LOGIN + " 입니다")
    @ApiResponse(
            responseCode = SwaggerDictionary.SUCCESS_CODE, description = SwaggerDictionary.SUCCESS,
            content = @Content(schema = @Schema(implementation = Map.class))    // TOKEN
    )
    @ApiErrorCodes({ ErrorCode.NOT_VALID, ErrorCode.UNAUTHORIZED, ErrorCode.NOT_FOUND })
    ResponseEntity<?> login(
            @RequestBody(
                    description = "Login Request Body",
                    required = true,
                    content = @Content(schema = @Schema(implementation = LoginRequest.class))
            ) LoginRequest request
    );

    @Operation(summary = SwaggerDictionary.SIGNUP, description = SwaggerDictionary.SIGNUP + " 입니다")
    @ApiSuccessCode(SuccessCode.OK)
    @ApiErrorCodes({ ErrorCode.NOT_VALID, ErrorCode.NOT_FOUND, ErrorCode.SAVE_ERROR })
    ResponseEntity<?> signup(
            @RequestBody(
                    description = "Member Request Body",
                    required = true,
                    content = @Content(schema = @Schema(implementation = MemberRequest.class))
            ) MemberRequest request
    );

}
