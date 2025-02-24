package com.lucas.r2dbcmariaflux.modules.member.repository;

import com.lucas.r2dbcmariaflux.modules.member.domain.Member;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface MemberRepository extends R2dbcRepository<Member, Long>, MemberCustomRepository {
}
