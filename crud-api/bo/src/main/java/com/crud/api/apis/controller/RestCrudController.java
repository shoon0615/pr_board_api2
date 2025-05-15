/**
 * packageName  : com.crud.api.apis.controller
 * fileName     : RestCrudController
 * author       : SangHoon
 * date         : 2025-01-07
 * description  : 메뉴1 > 메뉴2 RestController
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-01-07          SangHoon             최초 생성
 */
package com.crud.api.apis.controller;

import com.crud.api.apis.schema.RestCrudControllerDocs;
import com.crud.biz.service.CrudService;
import com.crud.common.dto.response.ApiResponse;
import com.crud.domain.domains.dto.request.CrudRequest;
import com.crud.domain.domains.dto.request.CrudSearchCondition;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/crud")
public class RestCrudController implements RestCrudControllerDocs {

    private final CrudService crudService;

    /**
     * 조회(단건)
     * @method       : findByCrud
     * @author       : SangHoon
     * @date         : 2025-01-08 AM 2:56
     */
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<?> findByCrud(@PathVariable final long id) {
        return ApiResponse.ok(crudService.findByCrud(id));
    }

    /**
     * 조회(전체)
     * @method       : findAllCrud
     * @author       : SangHoon
     * @date         : 2025-01-08 AM 2:56
     */
    @Override
    @GetMapping("/all")
//    public ResponseEntity findAllCrud(@Valid final CrudRequest request) {
    public ResponseEntity<?> findAllCrud(final CrudSearchCondition request) {
        return ApiResponse.ok(crudService.findAllCrud(request));
    }

    /**
     * 조회(페이징)
     * @method       : findPageCrud
     * @author       : SangHoon
     * @date         : 2025-05-13 AM 11:30
     */
    @Override
    @GetMapping
//    public ResponseEntity findPageCrud(final CrudSearchCondition request, @PageableDefault(size = 10) final Pageable pageable) {
    public ResponseEntity<?> findPageCrud(final CrudSearchCondition request) {
//        Pageable pageable = PageRequest.of(request.getPage(), request.getSize());     // JPA 의 class 라 biz 로 이동
        return ApiResponse.ok(crudService.findPageCrud(request));
    }

    /**
     * 입력
     * @method       : insertCrud
     * @author       : SangHoon
     * @date         : 2025-01-08 AM 2:56
     */
    @Override
    @PostMapping
    public ResponseEntity<?> insertCrud(@Valid @RequestBody final CrudRequest request) {
        crudService.insertCrud(request);
        return ApiResponse.ok();
    }

    /**
     * 수정(전체) -> merge??
     * @method       : saveCrud
     * @author       : SangHoon
     * @date         : 2025-01-08 AM 2:56
     */
    /*@PatchMapping("/{id}")
    public ResponseEntity<?> saveCrud(@PathVariable final long id, @Valid @RequestBody final CrudRequest request) {
        crudService.saveCrud(id, request);
        return ApiResponse.ok();
    }*/

    /**
     * 수정(일부)
     * @method       : updateCrud
     * @author       : SangHoon
     * @date         : 2025-01-08 AM 2:56
     */
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCrud(@PathVariable final long id, @Valid @RequestBody final CrudRequest request) {
        crudService.updateCrud(id, request);
        return ApiResponse.ok();
    }

    /**
     * 삭제
     * @method       : deleteCrud
     * @author       : SangHoon
     * @date         : 2025-01-08 AM 2:56
     */
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCrud(@PathVariable final long id) {
        crudService.deleteCrud(id);
        return ApiResponse.ok();
    }

}
