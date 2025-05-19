/**
 * packageName  : com.crud.domain.domains.repository
 * fileName     : BoardEntityRepositoryTest
 * author       : SangHoon
 * date         : 2025-05-18
 * description  :
 * ===========================================================
 * DATE                 AUTHOR                NOTE
 * -----------------------------------------------------------
 * 2025-05-18          SangHoon             최초 생성
 */
package com.crud.domain.domains.repository;

import com.crud.common.util.ObjectMapperUtil;
import com.crud.domain.config.jpa.JpaConfigTest;
import com.crud.domain.domains.dto.request.BoardRequest;
import com.crud.domain.domains.entity.board.BoardEntity;
import com.crud.domain.domains.entity.board.BoardTagEntity;
import com.fasterxml.jackson.core.type.TypeReference;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
@DataJpaTest
@Import(JpaConfigTest.class)
@ContextConfiguration(classes = com.crud.api.CrudApiBoApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BoardEntityRepositoryTest {

    @Autowired
    private BoardEntityRepository boardRepository;

    @Autowired
    private BoardTagEntityRepository boardTagRepository;

    @Autowired
    private BoardLikeEntityRepository boardLikeRepository;

    private BoardRequest request;

    @BeforeEach
    void setUp() {
        request = BoardRequest.builder()
                .title("테스트_제목")
                .contents("테스트_내용")
                .tags(List.of())
                .build();
    }

    @DisplayName("입력")
    @Test
    @Rollback(false)
    void save() throws Exception {
        BoardEntity entity = ObjectMapperUtil.convertType(request, new TypeReference<>() {});

        // TagRequest -> toEntity || fromDto
        List<Map<String, String>> tagRequest = request.getTags().stream().map(tag -> Map.of("contents", tag)).toList();
        List<BoardTagEntity> tagEntity = ObjectMapperUtil.convertType(tagRequest, new TypeReference<>() {});
//        boardTagRepository.saveAll(entity);

        // CascadeType.PERSIST 활용
        entity.addTags(tagEntity);
        BoardEntity result = boardRepository.save(entity);

        // TODO: @MapsId 빼니까 된듯??
        /*BoardEntity bb = BoardEntity.builder()
                .title("테스트_제목")
                .contents("테스트_내용")
                .build();
        bb.addTag(BoardTagEntity.builder().contents("테스트").build());
        BoardEntity result = boardRepository.save(bb);*/

        Long id = result.getId();
//        BoardEntity response = boardRepository.findByIdOrElseThrow(id);
        BoardEntity response = boardRepository.findByIdOrElseNull(id);

        assertThat(response)
                .extracting("title", "contents")
                .contains(request.getTitle(), request.getContents());

        /*String[] IGNORE_PROPERTIES = {"id", "deletedAt", "createdDate", "modifiedDate", "createdBy", "modifiedBy"};
        assertThat(response)
                .usingRecursiveComparison()
                .ignoringFields(IGNORE_PROPERTIES)
                .ignoringFields("hits")
                .isEqualTo(request);*/
    }

    @Test
    @Rollback(false)
    void temp() throws Exception {
        // 0. id 가 존재하면, @MapsId + CascadeType.PERSIST 가능 -> 단, insert 가 안됨(id 없어서)
        Map<String, Object> request = Map.of(
                "title", "테스트_제목",
                "contents", "테스트_내용",
                "hits", 1,
                "like", List.of(Map.of(
                        "boardId", 2,
                        "memberId", "test"
                )),
                "tag", List.of(Map.of(
                        "boardId", 2,
                        "contents", "테스트_태그"
                ))
        );
        BoardEntity entity = ObjectMapperUtil.convertType(request, new TypeReference<>() {});
        boardRepository.save(entity);

        // 1. CascadeType.PERSIST 활용
//        entity.addTag(tag);
        /*entity.addAllTag(tags);
        boardRepository.save(entity);*/

        // 2. 수동
        /*BoardEntity entity = ObjectMapperUtil.convertType(request, new TypeReference<>() {});
        boardRepository.save(entity);

        List<Map<String, String>> tags = request.getTags().stream().map(tag ->
                Map.of(
                        "boardId", entity.getId(),
                        "contents", tag
                )
        ).toList();
        List<BoardTagEntity> tagEntity = ObjectMapperUtil.convertType(tags, new TypeReference<>() {});
        boardTagRepository.saveAll(tagEntity);*/
    }

    @DisplayName("@MapsId + cascade 테스트(입력)")
    @Test
    @Rollback(false)
    void cascadeInsert() throws Exception {
        BoardEntity entity = ObjectMapperUtil.convertType(request, new TypeReference<>() {});

        List<Map<String, String>> tagRequest = request.getTags().stream().map(tag -> Map.of("contents", tag)).toList();
        List<BoardTagEntity> tagEntity = ObjectMapperUtil.convertType(tagRequest, new TypeReference<>() {});

        // 1. @MapsId 사용 + CascadeType.ALL -> (실패) board 는 영속성으로 적용되지만, boardId 는 null 이 될수밖에 없어 fk 적용 실패
        /*entity.addTags(tagEntity);
        boardRepository.save(entity);*/

        // 2. @MapsId 미사용 + CascadeType.ALL -> [채택] 성공
        entity.addTags(tagEntity);
        boardRepository.save(entity);

        // 3. @MapsId 사용 + CascadeType.REMOVE -> 성공
        /*BoardEntity result = boardRepository.save(entity);
        tagEntity.forEach(tag -> tag.addBoard(result));
        boardTagRepository.saveAll(tagEntity);*/
    }

    @DisplayName("@MapsId + cascade 테스트(수정)")
    @ParameterizedTest
    @ValueSource(longs = {20})
    @Rollback(false)
    void cascadeUpdate(Long id) throws Exception {
        BoardEntity entity = boardRepository.findByIdOrElseThrow(id);

        // 1. @MapsId 사용 + CascadeType.ALL -> 가능
        /*List<Map<String, String>> tagRequest = request.getTags().stream().map(tag -> Map.of("contents", tag)).toList();
        List<BoardTagEntity> tagEntity = ObjectMapperUtil.convertType(tagRequest, new TypeReference<>() {});
        tagEntity.forEach(tag -> tag.addBoard(entity));
        entity.addTags(tagEntity);
        boardRepository.save(entity);*/

        // 2. @MapsId 미사용 + CascadeType.ALL -> [채택] 성공
        List<Map<String, String>> tagRequest = request.getTags().stream().map(tag -> Map.of("contents", tag)).toList();
        List<BoardTagEntity> tagEntity = ObjectMapperUtil.convertType(tagRequest, new TypeReference<>() {});
        entity.addTags(tagEntity);
        boardRepository.save(entity);

        // 3. @MapsId 사용 + CascadeType.REMOVE -> 성공
        /*List<?> tagRequest = request.getTags().stream().map(tag ->
                Map.of("boardId", entity.getId(), "contents", tag)
        ).toList();

//        List<Map<String, Object>> tagRequest = request.getTags().stream().map(tag -> {
//            Map<String, Object> result = new HashMap<>();
//            result.put("boardId", entity.getId());
//            result.put("contents", tag);
//            return result;
//        }).toList();

        List<BoardTagEntity> tagEntity = ObjectMapperUtil.convertType(tagRequest, new TypeReference<>() {});
        boardTagRepository.saveAll(tagEntity);*/
    }

    @DisplayName("@MapsId + cascade 테스트(삭제)")
    @ParameterizedTest
    @ValueSource(longs = {28})
    @Rollback(false)
    void cascadeDelete(Long id) throws Exception {
        // @MapsId 여부 상관없이 가능
        boardRepository.deleteById(id);
    }

    @DisplayName("JPQL 테스트")
    @ParameterizedTest
    @ValueSource(longs = {2, 3})
    void jpql(Long id) throws Exception {
//        BoardEntity entity = boardRepository.findByTitle("테스트_제목2");
//        assertThat(entity).isNotNull();

        BoardEntity byId = boardRepository.findByIdOrElseNull(id);
//        BoardEntity test1 = boardRepository.findByMember_Id("test");
        BoardEntity test2 = boardRepository.findByIdAndMember_Id(id, "test");

        BoardEntity byIdBoardLike = boardRepository.findById_BoardLike(id);
        BoardEntity byIdBoardLikeAndMemberIdTest = boardRepository.findById_BoardLikeAndMember_IdTest(id);

        BoardLikeEntityRepository test = boardLikeRepository.findByBoardIdAndMemberId(id, "test");

        log.info("""
                byId: {}
                test2: {}
                byIdBoardLike: {}
                byIdBoardLikeAndMemberIdTest: {}
                test: {}
                end
        """,
                byId, test2, byIdBoardLike, byIdBoardLikeAndMemberIdTest, test
        );
    }

    @DisplayName("JPQL 테스트2")
    @ParameterizedTest
    @ValueSource(longs = {2, 3})
    void jpql2(Long id) throws Exception {
        BoardLikeEntityRepository test = boardLikeRepository.findByBoardIdAndMemberId(id, "test");
        long test1 = boardLikeRepository.countByBoardIdAndMemberId(id, "test");
        long test2 = boardLikeRepository.countByBoard_IdAndMember_Id(id, "test");
        boolean test3 = boardLikeRepository.existsByBoardIdAndMemberId(id, "test");

        log.info("""
                test: {}
                test1: {}
                test2: {}
                test3: {}
                end
        """,
                test, test1, test2, test3
        );
    }

}
