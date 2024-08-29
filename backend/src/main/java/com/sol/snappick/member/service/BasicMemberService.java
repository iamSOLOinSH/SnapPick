package com.sol.snappick.member.service;

import com.sol.snappick.member.entity.Member;
import com.sol.snappick.member.exception.BasicNotFoundException;
import com.sol.snappick.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import static org.mariadb.jdbc.plugin.authentication.standard.ed25519.Utils.bytesToHex;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BasicMemberService {

    private final MemberRepository memberRepository;

    @SneakyThrows
    public static String encode(String text) {
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedhash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(encodedhash);
    }

    // 핀코드 일치여부 확인
    public Boolean isCorrectPin(Integer memberId, String pinCode) {
        String origin = getMemberById(memberId).getPinCode();
        return origin.equals(encode(pinCode));
    }

    public Member getMemberById(Integer memberId) {
        return memberRepository.findById(memberId).orElseThrow(
                () -> new BasicNotFoundException("Not Found Member : MemberId is " + memberId)
        );
    }

    public Member getMemberById(String memberIdStr) {
        return memberRepository.findById(Integer.parseInt(memberIdStr)).orElseThrow(
                () -> new BasicNotFoundException("Not Found Member : MemberId is " + memberIdStr)
        );
    }

    public Member getMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(
                () -> new BasicNotFoundException("Not Found Member : email is " + email)
        );
    }
}
