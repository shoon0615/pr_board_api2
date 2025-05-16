# SpringBoot-SideProject-BULK
스프링 부트 Bulk 작업

## 기본 구조
- **기본**
1. `GET` 조회
   1. `/id` [단일] Projections.constructor
   2. `/all` [전체] Projections.constructor
   3. `/` [페이징] PageableExecutionUtils.getPage
2. `POST` 입력
   1. `/` [단일] JpaRepository.save
   2. `/ids` [복사] nativeQuery
   3. `/all` [전체] JpaRepository.saveAll
3. `PUT` 수정
   1. `/id` [단일] Dirty Checking
   2. `/ids` [복사] Dirty Checking
      - ids -> 모든 ids 의 기존 데이터에 수정
      - ids, request -> 모든 ids 를 request 로 수정
      - batch 나 api 에서만 사용할 것으로 보임
   3. `/all` [전체] Dirty Checking
      - request -> request 에 id 필요
      - 각 request 에 각각의 수정 작업 진행
4. `DELETE` 삭제 -> Soft Delete
   1. `/id` [단일] JpaRepository.delete
   2. `/ids` [일부] JpaRepository.deleteAll
   3. `/all` [전체] JpaRepository.deleteAll

- **심화**
1. `GET` 조회
   1. `/id` [단일] Projections.constructor || QResponse(@QueryProjection) -> QueryDSL 강결합 제거
      - `selectPeopleCondition` DTO(Projections || QResponse) 는 Entity 를 직접 반환하지 않기 때문에 fetch join 필요 없음
      - `wherePeopleCondition` BooleanExpression || BooleanBuilder -> 원래 조건에 null 이면 에러 발생하지만 supplier 를 통한 null-safe 작업 처리
   2. `/all` [전체] Projections.constructor || QResponse(@QueryProjection) -> QueryDSL 강결합 제거
      - transform(GroupBy.groupBy, GroupBy.list, ExpressionUtils.as)
   3. `/` [페이징] PageableExecutionUtils.getPage || PageImpl
      - count() 쿼리 필요
      - Pageable, offset, limit, OrderByCondition
2. `POST` 입력
   1. `/` [단일] JpaRepository.save
   2. `/ids` [복사] nativeQuery || MyBatis
   3. `/all` [전체] JpaRepository.saveAll || QueryDSL - JDBC batchInsert || JPA - order_inserts(persist)
      1. `saveAll` N번의 쿼리 발생 -> 성능 저하
      2. `batchInsert` 성능은 우월한데 cascade, orphanRemoval, callBack(@PrePersist, @PostUpdate...) 등.. 적용 불가 -> 단순 처리 또는 batch 용
      3. `order_inserts` [기존] saveAll 이용
         - IDENTITY(AUTO) 사용 시 @Id 문제로 saveAll 진행해도 그룹화 안됨(SEQUENCE 로 변경해야 가능) -> N번의 쿼리 발생
      4. `order_inserts` [임의] cascade 가능, batch_size 필요 -> 성능 우월, 안전성
         - 수동으로 order_inserts 똑같이 구현하여 처리
3. `PUT` 수정
   1. `/id` [단일] Dirty Checking
   2. `/ids` [복사] for(find ~ update(Dirty Checking)) || @Modifying @Query || QueryDSL || nativeQuery
      - for(find ~ update(Dirty Checking)) || saveAll 외에는 cascade 및 콜백 적용 불가
      - `setPeopleCondition` null 및 조건부에 따른 set 설정 여부 조작
   3. `/all` [전체] JpaRepository.saveAll || QueryDSL - JDBC batchUpdate || JPA - order_updates(merge || find ~ update(Dirty Checking))
      1. `saveAll` N번의 쿼리 발생 -> 성능 저하
      2. `batchUpdate` 성능은 우월한데 cascade 및 콜백 적용 불가 -> 단순 처리 또는 batch 용
      3. `order_updates` [기존] saveAll 이용
         - 같은 엔티티에 대한 update 를 그룹화하고 순서를 재조정해 SQL 을 효율적으로 배치 처리
         - request 에 연관관계 entity 정보가 없거나, null 인 컬럼은 그대로 반영(덮어쓰기 - replace)
         - 반영되지 않는 경우, SQL 이 다르면 (set a=?, b=? vs set a=?) PreparedStatement 가 달라져서 그룹화 안됨 -> N번의 쿼리 발생
         - saveAll() 이후, 직접 flush() 를 호출하지 않으면 JPA 에서 임의로 결정
         - saveAll 을 사용하지 않더라도 그룹화 진행되나, 앞선 설명과 같이 SQL 이 다르면 그룹화 안됨
      4. `order_updates` [임의] cascade 가능, batch_size 필요 -> 성능 우월, 안전성
         - 수동으로 order_updates 똑같이 구현하여 처리
         - `merge` cascade, orphanRemoval 로 인해 자식 엔티티도 영향 받음(기존 자식이 삭제되거나 덮어쓰기 위험) -> 가급적 지양  
         - `find ~ update` em.find 후 entity.update 로 Dirty Checking 진행
           - SQL 일치 여부 상관없이 batch_size 만큼 그룹화해서 한번에 진행하기에 효율적이고, 직접 flush() 도 진행하여 안정적
4. `DELETE` 삭제 -> Soft Delete
   1. `/id` [단일] JpaRepository.delete || Dirty Checking
      - `JpaRepository.delete` 이미 조회된 entity 삭제
      - `JpaRepository.deleteById` Id 로 조회(deleted 체크) 후 삭제
      - `entity.delete` Dirty Checking
   2. `/ids` [일부] for(find ~ delete(Dirty Checking)) || @Modifying @Query || QueryDSL || nativeQuery
      - `Soft Delete 기준` deleteAll 외에는 cascade 및 콜백 적용 불가
      - `deleteAllByIdInBatch` 성능은 우월한데 cascade 및 콜백 적용 불가 -> 단순 처리 또는 batch 용
   3. `/all` [전체] JpaRepository.deleteAll -> em.flush 최적화??
      1. `deleteAll` -> N번 select + N번 delete 쿼리 발생 -> 성능 저하
         - cascade 적용을 위해 사용 -> findAll() + deleteAll() 을 통해 1번 select + N번 delete 로 단축
      2. `order_delete` [기존] None
      3. `order_delete` [임의] cascade 가능, batch_size 필요 -> 성능 우월, 안전성
         - `em.remove` entity 만 받을 수 있어 미리 조회 후 전달하여 진행 -> N+1 발생하므로 fetch join 으로 조회
         - cascade 를 적용하려면 무조건 N회 작업이 될 수 밖에 없어보임

---

## 🖥️ Tip
1. `Page<> findAll(Pageable)` 해당 방식도 존재
   - Fetch Join 이 진행되지 않아 실제로 사용하진 않을 것으로 보임 -> 존재 여부만 알아두기 
2. `batch_size` findAll, saveAll
   - `findAll` hibernate.default_batch_fetch_size -> LAZY 조회 시, 그룹화
   - `saveAll` jdbc.batch_size -> saveAll 시, SQL 형식이 동일한 내용을 그룹화
3. `DTO 변환` 관심사 분리
   1. `조회(R)` JpaRepository -> Projections.constructor || QResponse(@QueryProjection)
   2. `작업(CUD)` 
      1. `toEntity`
      2. `fromEntity` 정적 팩토리 메소드
      3. `Mapstruct` @Mapper 를 통해 class 를 따로 두어 확실한 관심사 분리 -> 새 class 생성 및 Mapstruct 강결합
      4. `ObjectMapper` 리플렉션으로 인한 성능 저하 이슈
