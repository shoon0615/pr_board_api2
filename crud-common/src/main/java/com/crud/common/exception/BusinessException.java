/**
 * packageName  : com.crud.common.exception
 * fileName     : BusinessException
 * author       : SangHoon
 * date         : 2025-01-20
 * description  : 사이드용 custom Exception
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-20          SangHoon             최초 생성
 */
package com.crud.common.exception;

import com.crud.common.enumeration.response.ResponseCode;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    public BusinessException(ResponseCode.ErrorCode errorCode) {
        super(errorCode.getMessage());
    }

    public BusinessException(String errorMessage) {
        super(errorMessage);
    }

}
