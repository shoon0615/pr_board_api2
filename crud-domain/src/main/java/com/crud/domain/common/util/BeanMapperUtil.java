/**
 * packageName  : com.crud.domain.common.util
 * fileName     : BeanMapperUtil
 * author       : SangHoon
 * date         : 2025-05-13
 * description  : @Setter 없이 기존 영속 객체(this)를 유지하며 Dirty checking 시키기위한 라이브러리
 *                  리플렉션을 사용하기에 성능 저하 이슈가 있을 수 있으며, 그 외 복잡한 request 나 entity 는 처리 못할 수도 있음
 *                  사이드에서 단순화 작업을 위한 유틸로, 실무에선 직접 toEntity 나 MapStruct 등을 이용해 안전하게 변환 필요
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-13          SangHoon             최초 생성
 */
package com.crud.domain.common.util;

import com.crud.common.enumeration.response.ResponseCode;
import com.crud.common.exception.BusinessException;
import jakarta.persistence.Entity;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@Slf4j
@UtilityClass
public class BeanMapperUtil {

    private final List<Class<?>> SKIPPED_TYPES = List.of(Collection.class, Map.class);   // 반복 생성을 막기 위한 상수로 분리

    /**
     * <pre>
     * `@Setter` 없이 기존 영속 객체(this)를 유지하며 Dirty checking 시키기위한 라이브러리
     * 리플렉션을 사용하기에 성능 저하 이슈가 있을 수 있으며, 그 외 복잡한 request 나 entity 는 처리 못할 수도 있음
     * 사이드에서 단순화 작업을 위한 유틸로, 실무에선 직접 toEntity 나 MapStruct 등을 이용해 안전하게 변환 필요
     * </pre>
     * @method       : copyProperties
     * @author       : SangHoon
     * @date         : 2025-05-13 PM 10:15
     */
    public void copyProperties(@NonNull Object source, @NonNull Object target) {
        /** source: @Getter 필수, target: @Setter 필수 -> @Entity 특성상 @Setter 사용이 불가능하여 리플렉션으로 변경 */
//        @Deprecated
//        BeanUtils.copyProperties(source, target);

        Class<?> sourceClass = source.getClass();
        Class<?> targetClass = target.getClass();
        List<String> failedFields = new ArrayList<>();

        while (sourceClass != null && sourceClass != Object.class) {
            for (Field sourceField : sourceClass.getDeclaredFields()) {
                sourceField.setAccessible(true);

                try {
                    Object value = sourceField.get(source);
                    if (Modifier.isStatic(sourceField.getModifiers()) ||    // static 필드 제외
                            value == null ||                                // 불필요한 덮어쓰기 방지
                            shouldSkipField(sourceField)                    // JPA 연관 관계(@Entity) 또는 컬렉션(List) 제외
                    ) continue;

                    Field targetField = findField(targetClass, sourceField.getName());
                    if (targetField == null ||
                            !targetField.getType().isAssignableFrom(sourceField.getType())   // 타입 불일치 방지
                    ) continue;

                    targetField.setAccessible(true);
                    targetField.set(target, value);

                } catch (Exception e) {
                    // 1. 로그만 남기고 skip (전체 실패 방지)
//                    log.error("Skip copying field {}: {}", sourceField.getName(), e.getMessage());

                    // 2. 하나라도 copy 실패 시, 전체 실패 처리
//                    throw new IllegalStateException("Failed to copy field '" + sourceField.getName() + "' from source to target", e);

                    // 3. 2번 + 실패한 목록 출력
                    failedFields.add(sourceField.getName());
                }
            }
            sourceClass = sourceClass.getSuperclass();      // 상속 필드 포함 처리
        }

        if (!failedFields.isEmpty()) {
            /*String errorMessage = failedFields.stream().collect(Collectors.joining(System.lineSeparator()));
            log.error("Failed to copy field: {}", errorMessage);*/
            log.error("Failed to copy field: {}", failedFields);
            throw new BusinessException(ResponseCode.ErrorCode.SAVE_ERROR);
        }
    }

    private boolean shouldSkipField(Field field) {
        Class<?> type = field.getType();
        if (type.isAnnotationPresent(Entity.class)) return true;
        return SKIPPED_TYPES.stream().anyMatch(t -> t.isAssignableFrom(type));
    }

    private Field findField(Class<?> clazz, String name) {
        while (clazz != null && clazz != Object.class) {
            try {
                return clazz.getDeclaredField(name);
            } catch (NoSuchFieldException e) {
                clazz = clazz.getSuperclass();
            }
        }
        return null;
    }
}