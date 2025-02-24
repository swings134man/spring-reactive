## r2dbc-maria-flux module

### - Webflux with R2DBC MariaDB
- Webflux 에서는 non-blocking 을 제공하지 않는 JPA 대신, R2DBC 를 사용하여 non-blocking 의 최대 효율을 사용한다.
- R2DBC 는 Reactive Relational Database Connectivity 의 약자로, Reactive Programming 을 지원하는 데이터베이스 연결을 위한 스펙이다.
  - Blocking Program 에 비해서 더 높은 High Concurrency(동시성) 를 필요로 할때 더 좋은 성능ㅇ르 제공함
  - 그에따라, Latency(지연률) 이 낮아지고, Throughput(처리량) 이 높아진다.
  - `R2DbcRepository` 를 통해 CRUD 기능을 제공하지만, JPA 처럼 완벽한 ORM 을 제공하지는 않는다.
    - `@Query` 나 query method 를 통해 직접 쿼리 작성도 가능하다.
    - `@Transactional` 어노테이션을 사용하기 위해서는 `TransactionManager` 를 직접 구현해야 한다.
    - JPA 와 같이 연관관계를 사용하기 위해서는 `@Transient` 어노테이션을 사용하여 구현한다.
    - 또한 `@Id` 어노테이션만을 Entity Class 에 사용하며 `@GeneratedValue` 는 사용하지 않는다.
      - `@Table` 어노테이션을 사용하여 테이블명을 지정할 수 있다.
    
#### R2DBC 는 JPA 처럼 완벽한 ORM 이 아니다.
- JPA 처럼 복잡한 Query 를 자동으로 처리해주지 않는다.
- 기본적인 CRUD 기능은 제공하지만 복잡한 쿼리는 직접 작성해야한다(SQL)
  - JOIN, GROUPING, AGGREGATION, SubQuery 등,,
- 이떄 `@Query` 의 경우 Native SQL 만 지원한다(JPQL 미지원), Mono, Flux Type Return 해야함
- QueryDsl 또한 제공하지 않는다.

#### 즉, JPA 를 대체하는것이 아닌, Non-Blocking 환경에서 Reactive DB 처리를 위한 TOOL 이라고 생각하면 된다.

- 직접 쿼리사용하는 예제 2가지
```java
// 1. @Query 사용
public interface SampleRepository extends R2dbcRepository<Sample, Long> {
    @Query("SELECT * FROM sample WHERE name = :name")
    Flux<Sample> findByName(String name);
}

// 2. DatabaseClient 사용
@Repository
public class SampleCustomRepository {
    
    @Autowired
    private final DatabaseClient client;
    
    public Flux<Smaple> findSampleByAgeRange(int minAge, int maxAge){
        return databaseClient.sql("SELECT * FROM users WHERE age BETWEEN :minAge AND :maxAge")
                .bind("minAge", minAge)
                .bind("maxAge", maxAge)
                .map((row, metadata) -> new User(
                        row.get("id", Long.class),
                        row.get("name", String.class),
                        row.get("age", Integer.class)
                ))
                .all();
    }
}
```

---

### R2DBC 에서의 @Transactional 
- R2DBC 는 Non-Blocking 환경에서 `Transaction` 을 관리해야 한다.
  - 다만 기존의 Transaction 의 경우 ThreadLocal 을 사용하여 Transaction 을 관리하는데, ThreadLocal 은 Single Thread 에서만 사용가능하다. 즉 (Blocking 환경이고 JPA 는 이를 차용한다)
- Spring 의 Reactive Transaction 지원 기능을 사용하여 `@Transaction` 을 사용할 수 있다.
  - 다만 이 경우 `@Transaction` 의 Method Return type 은 `Mono`, `Flux` 이어야 한다.
- 수동으로 `Transaction` 을 관리 할 수 있다
```java

// 1. 수동 Transaction 관리
@Service
@RequiredArgsConstructor
public class SampleService {
    
    private final SampleRepository repository;
    private final TransactionOperator operator;
    
    // 이외에 다른 방법들이 더 존재함.
    public Flux<Sample> saveSample(Sample sample){
        return operator.transactional(repository.save(sample));
    }
}
```

---

### R2DBC 연관관계 
- R2DBC 는 JPA 처럼 연관관계를 자동으로 처리해주지 않는다.
- 따라서 `@Transient` 어노테이션을 사용하여 연관관계를 직접 구현해야 한다.
  - `@Transient` 는 DB 에 저장되지 않는 필드를 지정할때 사용한다.
  - `@Transient` 를 사용하여 연관관계를 구현하면, `@Query` 를 통해 직접 쿼리를 작성할때, 연관관계를 사용할 수 있다.
- 즉, `JOIN` 이나, `A` 테이블 조회후 연관된 `B` 테이블을 조회후 Mapping 하는 방법들을 사용해야 한다.
- 이때 Custom 한 Converter 나, Mapper 등을 구현하여 사용하는 방법도 존재한다. 
  - 단점은 연관관계가 많아지면 그만큼 Class 가 복잡해지고 수가 많아지게된다..
  - (Reading, Writer Converter Annotation) 을 사용하여 R2DbcCustomConversion 에 등록하여 사용하는 방법도 있다. 
- 주의점
  - 1:N 연관관계일때 Result 가 파편화 되어있을 수 있다.(하나의 list 로 묶이지 않는)
  - 이때 `.bufferedUntilChanged()` 로 동등비교 후 하나로 묶어 return 하면 된다.