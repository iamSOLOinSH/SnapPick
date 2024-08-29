package com.sol.snappick.member.service;

import com.sol.snappick.member.auth.TokenService;
import com.sol.snappick.member.dto.DetailMemberInfoRes;
import com.sol.snappick.member.dto.MemberRegisterReq;
import com.sol.snappick.member.dto.SimpleMemberInfoRes;
import com.sol.snappick.member.entity.Member;
import com.sol.snappick.member.entity.Role;
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
    private final BasicMemberService basicMemberService;
    private final TransactionService transactionService;
    private final TokenService tokenService;

    // 회원가입
    public SimpleMemberInfoRes register(Integer memberId,
                                        MemberRegisterReq memberRegisterReq) {
        Member member = basicMemberService.getMemberById(memberId);

        // 계정 생성!!
        String userKey = transactionService.postMember(member.getEmail());

        //판매자면
        if (memberRegisterReq.getRole() == 1) {
            // 사업자번호
            member.setBusinessNumber(memberRegisterReq.getBusinessNumber());
            // 계좌 생성!!
            member.setAccountNumber(transactionService.createAccount(userKey));
        }

        member.setRole(Role.values()[memberRegisterReq.getRole()]);
        member.setUserKey(userKey);
        member.setPinCode(encode(memberRegisterReq.getPinCode()));
        member.setPhoneNumber(memberRegisterReq.getPhoneNumber());

        memberRepository.save(member);
        return SimpleMemberInfoRes.fromEntity(member);
    }

    // 회원정보 확인
    @Transactional(readOnly = true)
    public SimpleMemberInfoRes getSimpleMemberInfo(Integer memberId) {
        Member member = basicMemberService.getMemberById(memberId);
        return SimpleMemberInfoRes.fromEntity(member);
    }

    // 핀코드 일치여부 확인
    @Transactional(readOnly = true)
    public Boolean isCorrectPin(Integer memberId, String pinCode) {
        String origin = basicMemberService.getMemberById(memberId).getPinCode();
        return origin.equals(encode(pinCode));
    }

    // 핀코드 재설정
    public void resetPin(Integer memberId, String pinCode) {
        Member member = basicMemberService.getMemberById(memberId);
        // TODO 본인인증 로직 추가
        member.changePinCode(encode(pinCode));
        memberRepository.save(member);
    }

    //////////////////////////////////////////////// 개발용

    @Transactional(readOnly = true)
    public List<DetailMemberInfoRes> getMemberInfo(String value, String type) {

        List<DetailMemberInfoRes> response = new ArrayList<>();
        if (type.equals("token")) {
            Integer memberId = Integer.parseInt(tokenService.getClaims(value).getSubject());
            Member member = basicMemberService.getMemberById(memberId);
            response.add(DetailMemberInfoRes.fromEntity(member));
        } else if (type.equals("email")) {
            Member member = basicMemberService.getMemberByEmail(value);
            response.add(DetailMemberInfoRes.fromEntity(member));
        } else if (type.equals("id")) {
            Member member = basicMemberService.getMemberById(value);
            response.add(DetailMemberInfoRes.fromEntity(member));
        } else if (type.equals("all")) {
            return memberRepository.findAll().stream().map(DetailMemberInfoRes::fromEntity).toList();
        }
        return response;
    }


    @SneakyThrows
    private String encode(String text) {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedhash);
    }
}
