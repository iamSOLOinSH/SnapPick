package com.sol.snappick.member.service;

import com.sol.snappick.member.dto.MemberRegisterReq;
import com.sol.snappick.member.dto.SimpleMemberInfoRes;
import com.sol.snappick.member.entity.Member;
import com.sol.snappick.member.exception.MemberNotFoundException;
import com.sol.snappick.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import static org.mariadb.jdbc.plugin.authentication.standard.ed25519.Utils.bytesToHex;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TransactionService transactionService;

    public SimpleMemberInfoRes register(Integer memberId,
                                        MemberRegisterReq memberRegisterReq) {
        Member member = memberRepository.findById(memberId).orElseThrow(
                () -> new MemberNotFoundException("Not Found Member : MemberId is " + memberId)
        );


        // 계정 생성 후, userKey 저장 (이미 존재하면 에러)
        String userKey = transactionService.postMember(member.getEmail());

        //TODO 계좌생성
        member.init(memberRegisterReq.getRole(),
                userKey,
                encode(memberRegisterReq.getPinCode()),
                memberRegisterReq.getPhoneNumber(),
                null,
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


    ////////////////////////////////////////////////

    @SneakyThrows
    private String encode(String text) {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedhash);
    }
}
