package com.lucas.r2dbcmariaflux.modules.post.domain;

import com.lucas.r2dbcmariaflux.config.BaseTimeEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("post") // R2DBC 는 @Entity 지원X
@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false) // BaseTimeEntity 상속받은 필드까지 비교(true), 안받은 필드만 비교(false)
public class Post extends BaseTimeEntity {

    @Id
    private Long id;
    private String title;
    private String content;
    private Long authorId;
}
