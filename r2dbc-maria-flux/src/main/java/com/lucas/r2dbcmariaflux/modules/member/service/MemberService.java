package com.lucas.r2dbcmariaflux.modules.member.service;

import com.lucas.r2dbcmariaflux.modules.member.domain.Member;
import com.lucas.r2dbcmariaflux.modules.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public Mono<Member> saveMember(Member member) {
        if(member.getId() == null) {
            return createMember(member);
        }else {
            return updateMember(member);
        }
    }

    private Mono<Member> createMember(Member member) {
        return memberRepository.save(member)
                .doOnSuccess(m -> log.info("Member saved: {}", m));
    }

    private Mono<Member> updateMember(Member member) {
        return memberRepository.findById(member.getId())
                .flatMap(m -> {
                    m.setId(member.getId());
                    m.setUserName(member.getUserName());
                    return memberRepository.save(m);
                })
                .doOnSuccess(m -> log.info("Member updated: {}", m));
    }


    @Transactional(readOnly = true)
    public Flux<Member> findAllMembers() {
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Mono<Member> findMemberById(Long id) {
        return memberRepository.findById(id);
    }

    // ---------------------------------------- Custom Query ----------------------------------------
}
