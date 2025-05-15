/**
 * packageName  : com.crud.domain.common.converter
 * fileName     : BooleanToYnConverter
 * author       : SangHoon
 * date         : 2025-01-07
 * description  : Boolean 형태를 자동으로 변환(true: 'Y', false: 'N')
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-07          SangHoon             최초 생성
 */
package com.crud.domain.common.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public final class BooleanToYnConverter implements AttributeConverter<Boolean, String> {

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return "Y".equals(dbData);
    }

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? "Y" : "N";
    }

}
