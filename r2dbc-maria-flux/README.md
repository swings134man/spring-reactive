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