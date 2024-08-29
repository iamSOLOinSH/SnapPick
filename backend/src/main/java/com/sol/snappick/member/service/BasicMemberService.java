package com.sol.snappick.member.service;

import com.sol.snappick.member.entity.Member;
import com.sol.snappick.member.exception.BasicNotFoundException;
import com.sol.snappick.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional
@RequiredArgsConstructor
public class BasicMemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Member getMemberById(Integer memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new BasicNotFoundException("Not Found Member : MemberId is " + memberId)
        );
    }

    @Transactional(readOnly = true)
    public Member getMemberById(String memberIdStr) {
        return memberRepository.findById(Integer.parseInt(memberIdStr)).orElseThrow(
                () -> new BasicNotFoundException("Not Found Member : MemberId is " + memberIdStr)
        );
    }

    @Transactional(readOnly = true)
    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(
                () -> new BasicNotFoundException("Not Found Member : email is " + email)
        );
    }
}
