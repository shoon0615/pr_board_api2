/**
 * packageName  : com.crud.domain.common.repository
 * fileName     : JpaRepositoryExtension
 * author       : SangHoon
 * date         : 2025-01-07
 * description  : JpaRepository 사용 시 추가 기능 제공
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-07          SangHoon             최초 생성
 */
package com.crud.domain.common.repository;

import com.crud.common.exception.BusinessException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import static com.crud.common.enumeration.response.ResponseCode.ErrorCode;

@NoRepositoryBean
public interface JpaRepositoryExtension<T, ID> extends JpaRepository<T, ID> {

    default T findByIdOrElseNull(ID id) {
        return findById(id).orElse(null);
    }

    default T findByIdOrElseThrow(ID id) {
//        return this.findByIdOrElseThrow(id, id + " 는(은) 존재하지 않는 ID 입니다.");     // 보안 상 제거(default: "No value present")
        return findById(id).orElseThrow(() -> new BusinessException(ErrorCode.FIND_ERROR));
    }

    default T findByIdOrElseThrow(ID id, String errorMessage) {
        return findById(id).orElseThrow(() -> new BusinessException(errorMessage));
    }

}
