package com.lucas.r2dbcmariaflux.modules.member.domain;

import com.lucas.r2dbcmariaflux.config.BaseFieldEntity;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("member")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Member extends BaseFieldEntity {

    @Id
    private Long id;
    private String userName;

}
