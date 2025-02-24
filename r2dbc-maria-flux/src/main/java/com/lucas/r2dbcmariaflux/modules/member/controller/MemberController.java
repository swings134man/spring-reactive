package com.lucas.r2dbcmariaflux.modules.member.controller;

import com.lucas.r2dbcmariaflux.modules.member.domain.Member;
import com.lucas.r2dbcmariaflux.modules.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/member")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public Mono<ResponseEntity<Member>> saveMember(@RequestBody Member member) {
        return memberService.saveMember(member)
                .map(data -> ResponseEntity.status(HttpStatus.CREATED).body(data));
    }

    @GetMapping
    public Mono<ResponseEntity<List<Member>>> findAllMembers() {
        return memberService.findAllMembers()
                .collectList()
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Member>> findMemberById(@PathVariable Long id) {
        return memberService.findMemberById(id)
                .map(ResponseEntity::ok);
    }

}
