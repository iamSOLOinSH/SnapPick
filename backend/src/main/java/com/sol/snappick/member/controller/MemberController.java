package com.sol.snappick.member.controller;

import com.sol.snappick.member.dto.MemberRegisterReq;
import com.sol.snappick.member.dto.SimpleMemberInfoRes;
import com.sol.snappick.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.sol.snappick.global.ApiDescriptions.header_token;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    @Operation(summary = "회원가입 정보 입력", description = header_token + """
            <b> Input : </b>
            \n
            <b>MemberRegisterReq</b>
            \n
            | Name | Type  | Description |
            |-----|-----|-------|
            | role | int | 구매자(0) 혹은 판매자(1) |
            | phoneNumber | varchar(15) | 핸드폰 번호 |
            | businessNumber | varchar(20) |  사업자 등록 번호(선택) |
            | pinCode | varchar(4) |  간편 비밀번호 |
                        
            \n
            \n
            <b> Output : </b>
            \n
            <b>SimpleMemberInfoRes</b>
            \n
            | Name | Type | Description |
            |-----|-----|-------|
            | name | string | 이름 |
            | profileImageUrl | string | 프로필 이미지 링크 |
            """)
    public ResponseEntity<SimpleMemberInfoRes> signup(
            Authentication authentication,
            @RequestBody MemberRegisterReq memberRegisterReq
    ) {
        Integer memberId = Integer.valueOf(authentication.getName());
        return ResponseEntity.ok().body(memberService.register(memberId, memberRegisterReq));
    }

    @GetMapping("/info")
    @Operation(summary = "회원 정보 확인", description = header_token + """
            <b>Output</b>:
            \n
            <b>SimpleMemberInfoRes</b>
            \n
            | Name | Type | Description |
            |-----|-----|-------|
            | name | string | 이름 |
            | profileImageUrl | string | 프로필 이미지 링크 |
            """)
    public ResponseEntity<SimpleMemberInfoRes> getSimpleInfo(
            Authentication authentication
    ) {
        Integer memberId = Integer.valueOf(authentication.getName());
        return ResponseEntity.ok().body(memberService.getSimpleMemberInfo(memberId));
    }
}
