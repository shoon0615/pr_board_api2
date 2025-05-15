/**
 * packageName  : com.crud.domain.common.dto.response
 * fileName     : PageResponse
 * author       : SangHoon
 * date         : 2025-04-23
 * description  : 페이징 OutDto
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-04-23          SangHoon             최초 생성
 */
package com.crud.domain.common.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * @sample Client
 * <pre>
 * const response = await axios.get('/boards?page=0&size=10');
 * const { data, pagination } = response.data;
 *
 * console.log(data); // 게시글 목록
 * console.log(pagination.totalElements); // 전체 게시글 수
 * </pre>
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PageResponse<T> {
    private List<T> data;
    private Pagination pagination;

    /**
     * @sample
     * <pre>
     * {
     *   "data": [],
     *   "pagination": {
     *     "page": 0,
     *     "size": 10,
     *     "totalElements": 120,
     *     "totalPages": 12,
     *     "hasNext": true,
     *     "last": false
     *   }
     * }
     * </pre>
     */
    @Getter
    @AllArgsConstructor
    public static class Pagination {
        private int page;
        private int size;
        private long totalElements;
        private int totalPages;
        private boolean hasNext;
        private boolean last;
    }

    /**
     * @sample
     * {@snippet :
     * @GetMapping("/boards")
     * public ResponseEntity<PageResponse<BoardResponse>> getBoards(Pageable pageable) {
     *     Page<BoardResponse> result = boardService.getBoards(pageable);
     *     return ResponseEntity.ok(PageResponse.from(result));
     * }
     *}
     */
    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(
                page.getContent(),
                new Pagination(
                        page.getNumber()
                        , page.getSize()
                        , page.getTotalElements()
                        , page.getTotalPages()
                        , page.hasNext()
                        , page.isLast()
                )
        );
    }
}