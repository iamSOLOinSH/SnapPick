package com.sol.snappick.member.service;

import com.sol.snappick.member.auth.TokenService;
import com.sol.snappick.member.dto.DetailMemberInfoRes;
import com.sol.snappick.member.dto.MemberRegisterReq;
import com.sol.snappick.member.dto.SimpleMemberInfoRes;
import com.sol.snappick.member.entity.Member;
import com.sol.snappick.member.entity.Role;
import com.sol.snappick.member.exception.BasicBadRequestException;
import com.sol.snappick.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static com.sol.snappick.member.service.BasicMemberService.encode;

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

        // for debug
        System.out.println(member.getUserKey());
        System.out.println(member.getUserKey() == null);
        if (member.getUserKey() != null) {
            throw new BasicBadRequestException("이미 정보를 입력한 회원입니다");
        }

        // 1. 계정 생성
        String userKey = transactionService.postMember(member.getEmail());

        //판매자면
        if (memberRegisterReq.getRole() == 1) {
            // 사업자번호
            member.setBusinessNumber(memberRegisterReq.getBusinessNumber());
            // 2. 계좌 생성
            member.setAccountNumber(transactionService.createAccount(userKey));
        }

        // 3. 저장
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

    // 핀코드 재설정
    public void resetPin(Integer memberId, String pinCode) {
        Member member = basicMemberService.getMemberById(memberId);
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

    public String generateToken(Integer memberId) {
        Member member = basicMemberService.getMemberById(memberId);
        return tokenService.generateToken(member, Duration.ofMinutes(10));
    }
}
