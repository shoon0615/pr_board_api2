# SpringBoot-SideProject-SOFT_DELETE
스프링 부트 DELETE 작업

## 기본 구조
- **한 줄 요약**
1. `Delete 방식`
    1. `Hard Delete` cascade : 연관된 자식객체 처리
        1. `Delete` JPA 레벨(JAVA) 처리
        2. `@OnDelete(action = OnDeleteAction.CASCADE)` DDMS 레벨(DB) 처리
    2. `Soft Delete` cascade
        1. `Dirty Checking` 불가
        2. `@SoftDelete` 가능
        3. `@SqlDelete` + `@SQLRestriction` 가능
2. `Cascade 방식`
    1. `cascade` JPA 레벨
    2. `orphanRemoval` JPA 레벨
    3. `@OnDelete` DB 레벨

---

- **Hard Delete**  
`데이터 미존재` 삭제 진행 시 실제 DB 에서 삭제  
  - DELETE FROM entity;
    1. `Delete` repository.delete()
       - `JPA 레벨` JAVA
    2. `@OnDelete(action)` 자식 데이터에서 참조하고 있을 경우
       - `DDMS 레벨` DB
       - Soft Delete 는 delete 쿼리를 사용하지 않기 때문에 사용 불가 -> 필요 시 DB 레벨에서 트리거로 직접 구현
       - `OnDeleteAction.NO_ACTION` `OnDeleteAction.RESTRICT` default: 불가
         - `ON DELETE RESTRICT` 부모를 참조하는 자식 데이터 존재 시, 삭제 불가
         - `ON UPDATE RESTRICT` 부모를 참조하는 자식 데이터 존재 시, ID 변경 불가
       - `OnDeleteAction.SET_NULL` 참조하는 부모 데이터 삭제 시, 참조(FK) 키값 NULL 로 변경
       - `OnDeleteAction.CASCADE` 참조하는 부모 데이터 삭제 시, 같이 삭제

- **Soft Delete**  
`데이터 존재` 삭제 진행 시 DB 의 삭제 컬럼 업데이트  
  - UPDATE entity SET deleted = true;
    1. `Dirty Checking` entity.delete()
       - 안정성, `Auditing` 적용, `cascade 및 콜백` 미적용
         - `콜백` cascade, orphanRemoval, callBack(@PrePersist, @PostUpdate...) 
    2. `@SoftDelete` repository.delete()
       - 모든 기능 최적화, `Auditing` 미적용, `cascade 및 콜백` 적용
       - `@ManyToOne(fetch = FetchType.LAZY)` 불가능 -> EAGER 만 가능
       - `@NotFound(action = NotFoundAction.IGNORE)` 적용 시, LAZY 에서도 가능하지만 FK 미생성 -> 연관 무결성 깨짐
    3. `@SqlDelete` + `@SQLRestriction` repository.delete()
       - `Auditing` 미적용, `cascade` 적용
         - delete 실행 시, nativeQuery 로 변환되어 실행
       - 각 Entity 에 선언 필요 -> 공통 처리 방법 못찾음
         - `@SQLDelete` `@Where` `@Loader` `@Filter` @Entity 가 선언된 클래스에만 적용 -> `@MappedSuperclass` 미적용 
       - `@Where` Hibernate 6.4 이전(join 미적용) -> `@SQLRestriction` Hibernate 6.4 이후(join 외 모두 적용)
       - `@SQLRestriction` nativeQuery 를 제외한 모든 조건에 작성한 조건문 적용
       - `@Filter` `@Loader` 조회용
         - `@FilterDef` 공통 조회용 -> @Filter 와 세트인데, @Filter 를 각 Entity 에 선언 필요
         - 이전에 @Where 어노테이션이 일부 작업에서 적용이 안되어 사용했으나 @SQLRestriction 부터는 굳이 필요없음

- **Tip**
1. `@SoftDelete`
   1. `자동변환` 적용
      - 기존대로 repository.delete 사용 시, 자동으로 soft delete 변환되어 실행
   2. `Auditing` 미적용
      - `LocalDateTime` 불가
      - 현재 @SoftDelete 는 LocalDateTime 을 지원할 수 없는 상태이다.  
        엄밀히 말하면, 컬럼이 Null 을 통해서 처리해야하는 경우가 불가능하다.  
        삭제일시가 필요한 경우 삭제 여부를 IS NULL 을 통해 삭제 여부를 따져야하는데,  
        @Converter 가 Null 을 허용하지 않는다!
2. `@SqlDelete`
   1. `자동변환` 미적용
      - nativeQuery 로 변환되기에 작성한 그대로 적용 -> Converter 사용 시, 적용된 내용으로 작성
   2. `Auditing` 미적용
      - `LocalDateTime` 가능 -> 컬럼 자체의 타입을 LocalDateTime 로 생성
      - `@Column(updatable = false)`
        - `JPA 레벨` 불가 -> 수정하더라도 SQL 쿼리 미생성
        - `DB 레벨` 허용 -> DB 상 수작업 또는 nativeQuery 적용 가능
          - @SqlDelete 의 쿼리는 nativeQuery 로 변환된 것과 같아 무시하고 적용됨
3. `@SQLRestriction`
   - nativeQuery 로 변환되기에 작성한 그대로 적용 -> Converter 사용 시, 적용된 내용으로 작성
   - 모든 repository 작업 시, 작성한 조건문 적용 -> JPQL, QueryDSL 포함
   - nativeQuery 작업 시, 미적용
4. `@OneToMany(orphanRemoval = true)` 고아객체 삭제
   - 부모 객체에서 자식 컬렉션을 제거했을 경우, 자식 객체를 삭제하고, 연관관계를 단절하는 설정
   - delete 미사용 -> Dirty Checking 유사(트랜잭션 종료될 때 delete 진행)
   - `setList(null)` 새로운 컬렉션 객체가 할당되면서 엔티티와의 참조가 끊어지기 때문에 기존 객체에 할당되도록 List 자체는 건들지말기
5. `@ManyToOne(optional = false)`
   - FK 를 not null 로 생성
   - 연관관계 join 시, outer join 대신 inner join 로 진행
   - 실무에선 여러 이유로 인해 FK 연결을 안하거나 null 허용하여, 잘 사용되진 않음
6. `@DynamicInsert` `@DynamicUpdate` 가급적 지양
   - `@DynamicInsert` INSERT 시 null 인 필드 제외
     - null 을 값으로 인지하지 못하여, 임의 null 처리가 안됨
     - 빈값 또는 작성하지 않은 필드는 모두 null 로 들어가는데, default 값(@ColumnDefault) 사용이 많을 경우, 사용을 고려할만함
   - `@DynamicUpdate` UPDATE 시 변경된 필드만 포함
     - 일반적으로 영속성 컨텍스트를 통해 변경된 필드만 수정 적용되기 때문에, 모든 필드를 update 하더라도 성능 차이가 거의 없음 -> 굳이 캐싱을 포기하면서까지의 이점이 없음
     - 오히려 동적으로 SQL 쿼리 생성이 되도록 변경되기 때문에 성능 감소가 생길 수 있음
   - 모든 필드를 사용하면 바인딩 되는 데이터만 다를 뿐, 등록/수정 쿼리가 항상 같아 SQL 쿼리 캐싱 및 재사용 용이

- **예제**
```java
@MappedSuperclass
//@SoftDelete(columnName = "delete_yn", converter = BooleanToYnConverter.class)
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 1. Dirty Checking
    @Column(nullable = false)
    @ColumnDefault("'N'")
    @Convert(converter = BooleanToYnConverter.class)
    private boolean deleteYn = false;
    
    // 2. @SoftDelete -> 생략 가능
    @Setter(AccessLevel.NONE)
    @Column(name = "delete_yn", nullable = false, insertable = false, updatable = false)
    @ColumnDefault("'N'")
    @Check(constraints = "delete_yn IN ('Y', 'N')")
    private String deleteYn;

    // 3. @SqlDelete + @SQLRestriction
    @Column(updatable = false)
    protected LocalDateTime deletedAt;
}
```
```java
@Entity
@SQLDelete(sql = "UPDATE child_entity SET delete_yn = 'Y' WHERE id = ?")
@SQLRestriction("delete_yn = 'N'")
public class ChildEntity extends BaseEntity {
    @Setter(AccessLevel.PROTECTED)
    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ParentEntity parents;
}
```
```java
@Entity
@SQLDelete(sql = "UPDATE parent_entity SET delete_yn = 'Y' WHERE id = ?")
@SQLRestriction("delete_yn = 'N'")
public class ParentEntity extends BaseEntity {
    @OneToMany(mappedBy = "parents", cascade = CascadeType.ALL)
    private List<ChildEntity> childs = new ArrayList<>();

    // 직렬화 때문에 get/set 의 메소드 명칭은 지양
    public void addChild(ChildEntity child) {
        childs.add(child);      // 부모 -> 자식 방향의 객체 참조가 있어야 cascade 작동
        child.setParents(this); // 연관관계의 주인 설정 -> persist 동작 자체엔 불필요하지만 연관 무결성을 위해 작성
    }
}
```

- **Cascade**
1. `CascadeType.PERSIST` insert
```java
ParentEntity parent = ObjectMapperUtil.convertType(request, new TypeReference<>() {});
ChildEntity child = ObjectMapperUtil.convertType(request, new TypeReference<>() {});
//parent.getChilds().add(parent);      // 해당 상태만 진행 후 save 해도 persist 동작하지만 FK 없음
parent.addChild(child);
parentRepository.save(parent);
```
2. `CascadeType.MERGE` 이미 존재하는 id
```java
// 다른 id 에 entity 복사
ParentEntity parent = parentRepository.findByIdOrElseThrow(id);
ParentEntity parent_copy = ObjectMapperUtil.convertType(parent, new TypeReference<>() {});
parent_copy.setId(other_id);
parentRepository.save(parent_copy);

// 자식 entity 복사
ParentEntity parent = ObjectMapperUtil.convertType(request, new TypeReference<>() {});
ParentEntity parent_copy = parentRepository.findByIdOrElseThrow(id);
parent.getChilds().addAll(parent_copy.getChilds());
parent.setId(id);
parentRepository.save(parent);

// 자식만 수정
ParentEntity parent = parentRepository.findByIdOrElseThrow(id);
parent.getChilds().get(0).setColumn("");
parentRepository.save(parent);  // child 가 save

// request 가 이미 존재하는 id 인 경우(단, 주의 -> 존재하지 않는 id 면 persist 가 진행돼버림
ParentEntity parent = ObjectMapperUtil.convertType(request, new TypeReference<>() {});
parentRepository.save(parent);
```
3. `CascadeType.REMOVE` delete
```java
parentRepository.deleteById(id);
```
4. `CascadeType.ALL` PERSIST, MERGE, REMOVE 모두 포함