package com.sol.snappick.member.service;

import com.sol.snappick.member.auth.TokenService;
import com.sol.snappick.member.dto.DetailMemberInfoRes;
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
import java.util.ArrayList;
import java.util.List;

import static org.mariadb.jdbc.plugin.authentication.standard.ed25519.Utils.bytesToHex;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final TransactionService transactionService;
    private final TokenService tokenService;

    public SimpleMemberInfoRes register(Integer memberId,
                                        MemberRegisterReq memberRegisterReq) {
        Member member = getMemberById(memberId);

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
        Member member = getMemberById(memberId);
        return SimpleMemberInfoRes.fromEntity(member);
    }

    @Transactional(readOnly = true)
    public List<DetailMemberInfoRes> getMemberInfo(String value, String type) {

        List<DetailMemberInfoRes> response = new ArrayList<>();


        if (type.equals("token")) {
            Integer memberId = Integer.parseInt(tokenService.getClaims(value).getSubject());
            response.add(DetailMemberInfoRes.fromEntity(getMemberById(memberId)));
        } else if (type.equals("email")) {
            response.add(DetailMemberInfoRes.fromEntity(
                    memberRepository.findByEmail(value).orElseThrow(
                            () -> new MemberNotFoundException("Not Found Member : email is " + value)
                    )
            ));
        } else if (type.equals("id")) {
            response.add(DetailMemberInfoRes.fromEntity(getMemberById(value)));
        } else if (type.equals("all")) {
            return memberRepository.findAll().stream().map(DetailMemberInfoRes::fromEntity).toList();
        }
        return response;
    }

    ////////////////////////////////////////////////

    private Member getMemberById(Integer memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new MemberNotFoundException("Not Found Member : MemberId is " + memberId)
        );
    }

    private Member getMemberById(String memberIdStr) {
        return memberRepository.findById(Integer.parseInt(memberIdStr)).orElseThrow(
                () -> new MemberNotFoundException("Not Found Member : MemberId is " + memberIdStr)
        );
    }


    @SneakyThrows
    private String encode(String text) {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedhash);
    }
}
