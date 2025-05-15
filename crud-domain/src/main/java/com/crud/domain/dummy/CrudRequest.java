/**
 * packageName  : com.crud.domain.dummy
 * fileName     : CrudRequest
 * author       : SangHoon
 * date         : 2025-01-07
 * description  : 메뉴1 > 메뉴2 InDto
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-07          SangHoon             최초 생성
 */
package com.crud.domain.dummy;

/*@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@Schema(description = "메뉴1 > 메뉴2 InDto")
public class CrudRequest {

    @NotBlank(message = CrudRequestDocs.title_label + "{valid.not-blank}")
    @Size(min = 1, max = 500, message = CrudRequestDocs.title_label + "{valid.between}")
    @Schema(description = CrudRequestDocs.title_label, example = CrudRequestDocs.title_label, minLength = 1, maxLength = 500)
    private String title;

    @NotBlank(message = CrudRequestDocs.contents_label + "{valid.not-blank}")
    @Size(min = 1, message = CrudRequestDocs.contents_label + "{valid.min}")
    @Schema(description = CrudRequestDocs.contents_label, example = CrudRequestDocs.contents_label, minLength = 1)
    private String contents;

}*/

@Deprecated
public class CrudRequest {}