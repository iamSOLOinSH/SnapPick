package com.sol.snappick.member.service;

import com.sol.snappick.member.dto.MemberRegisterReq;
import com.sol.snappick.member.dto.SimpleMemberInfoRes;
import com.sol.snappick.member.entity.Member;
import com.sol.snappick.member.exception.MemberNotFoundException;
import com.sol.snappick.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public SimpleMemberInfoRes register(Integer memberId,
                                        MemberRegisterReq memberRegisterReq) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberNotFoundException("Not Found Member : MemberId is " + memberId)
        );
        
        //TODO 금융API 회원가입, 계좌생성, user-key저장

        member.init(memberRegisterReq.getRole(), null,
                memberRegisterReq.getPinCode(), memberRegisterReq.getPhoneNumber(), null,
                memberRegisterReq.getBusinessNumber());

        memberRepository.save(member);
        return SimpleMemberInfoRes.fromEntity(member);
    }

    @Transactional(readOnly = true)
    public SimpleMemberInfoRes getSimpleMemberInfo(Integer memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberNotFoundException("Not Found Member : MemberId is " + memberId)
        );
        return SimpleMemberInfoRes.fromEntity(member);
    }
}
