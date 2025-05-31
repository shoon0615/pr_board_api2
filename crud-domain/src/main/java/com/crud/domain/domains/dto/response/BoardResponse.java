/**
 * packageName  : com.crud.domain.domains.dto.response
 * fileName     : BoardResponse
 * author       : SangHoon
 * date         : 2025-05-17
 * description  :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-17          SangHoon             최초 생성
 */
package com.crud.domain.domains.dto.response;

import lombok.*;

import java.util.List;

@Getter
@ToString(callSuper = true)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BoardResponse {

    /** 제목 */
    private String title;

    /** 내용 */
    private String contents;

    /** 조회 수 */
    private int hits;

    /** 작성자 */
    private String memberName;

    /** 좋아요 수 */
    private int likes;

    /** 태그 목록 */
    private List<String> tags;

    /** 댓글 수 */
    private int reviews;

    @Builder
    public BoardResponse(String title, String contents, int hits, String memberName, int likes, List<String> tags, int reviews) {
        this.title = title;
        this.contents = contents;
        this.hits = hits;
        this.memberName = memberName;
        this.likes = likes;
        this.tags = tags;
        this.reviews = reviews;
    }

    @Getter
    @ToString(callSuper = true)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BoardReviewResponse {

        /** 리뷰 ID */
//        private long reviewId;
        private Long reviewId;

        /** 내용 */
        private String contents;

    }

}
