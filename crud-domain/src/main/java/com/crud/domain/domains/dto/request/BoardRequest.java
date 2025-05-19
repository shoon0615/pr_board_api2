/**
 * packageName  : com.crud.domain.domains.dto.request
 * fileName     : BoardRequest
 * author       : SangHoon
 * date         : 2025-05-17
 * description  : 게시판 작업용 InDto
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-17          SangHoon             최초 생성
 */
package com.crud.domain.domains.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public class BoardRequest {

    /** 제목 */
    @NotBlank(message = "제목" + "{valid.not-blank}")
    @Size(min = 1, max = 500, message = "제목" + "{valid.between}")
    private String title;

    /** 내용 */
    @NotBlank(message = "내용" + "{valid.not-blank}")
    @Size(min = 1, message = "내용" + "{valid.min}")
    private String contents;

    /** 태그 */
//    @NotNull(message = "태그" + "{valid.not-null}")       // List 필수 여부(required)
//    @Size(min = 1, message = "태그" + "{valid.size-min}") // List 가 null 이 아닐 경우, 사이즈 체크 -> [] 불가
//    private List<String> tags;
    private List<
            @NotBlank(message = "태그" + "{valid.not-blank}")
            @Size(min = 1, message = "태그" + "{valid.min}")
            String
    > tags;

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @SuperBuilder
//    public static class BoardReviewRequest extends PageCondition {
    public static class BoardReviewRequest {

        /*@NotNull(message = "리뷰 ID" + "{valid.not-null}")
//        private long reviewId;
        private Long reviewId;*/

        @NotBlank(message = "리뷰" + "{valid.not-blank}")
        @Size(min = 1, max = 2000, message = "리뷰" + "{valid.between}")
        private String contents;

    }

    // 한 DTO 에 조회용/작업용으로 사용??
//    public static class BoardSearchCondition {}

}
