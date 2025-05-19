/**
 * packageName  : com.crud.domain.common.dto.request
 * fileName     : PageCondition
 * author       : SangHoon
 * date         : 2025-04-24
 * description  : 페이징 InDto
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-04-24          SangHoon             최초 생성
 */
package com.crud.domain.common.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
public abstract class PageCondition {

//    @Min(value = 1, message = "페이지는 1 이상이어야 합니다.")
//    private int page;     // @NonNull
    private Integer page;

    /*public Integer getPage() {
        return page - 1;
    }*/

//    @Min(value = 10, message = "사이즈는 10 이상이어야 합니다.")
//    private Integer size;

    @JsonIgnore
    private int size = 10;

//    PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
    /*@JsonIgnore
    private Sort sort = Sort.by(Sort.Direction.DESC, "id");*/

    // TODO: 검색어 -> Inner Class?? || SearchCondition extends SearchWord extends PageCondition??
//    private Map<String, String> searchCondition;

    /*private PageCondition.SearchCondition searchCondition;

    @Getter
    private static class SearchCondition {  // public?? -> @Getter 리플렉션이라 필요없을듯??
        private String searchCategory;      // filter
        private String searchWord;
    }*/

}
