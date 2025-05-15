/**
 * packageName  : com.crud.common.enumeration.response
 * fileName     : BaseEnum
 * author       : SangHoon
 * date         : 2025-01-21
 * description  : ResponseCode 의 메소드 interface
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-21          SangHoon             최초 생성
 */
package com.crud.common.enumeration.response;

public interface BaseCode {
    String name();
    int getCode();
    String getMessage();
}
