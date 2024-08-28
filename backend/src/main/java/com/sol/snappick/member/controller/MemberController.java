package com.sol.snappick.member.controller;

import com.sol.snappick.member.dto.MemberRegisterReq;
import com.sol.snappick.member.dto.SimpleMemberInfoRes;
import com.sol.snappick.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static com.sol.snappick.global.ApiDescriptions.Headers.des_MemberRegisterReq;
import static com.sol.snappick.global.ApiDescriptions.Headers.des_header_token;
import static com.sol.snappick.global.ApiDescriptions.MemberController.des_SimpleMemberInfoRes;
import static com.sol.snappick.global.ApiDescriptions.common.des_input;
import static com.sol.snappick.global.ApiDescriptions.common.des_output;

@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/register")
    @Operation(summary = "회원가입 정보 입력",
            description = des_header_token + des_input + des_MemberRegisterReq + des_output + des_SimpleMemberInfoRes)
    public ResponseEntity<SimpleMemberInfoRes> signup(
            Authentication authentication,
            @RequestBody MemberRegisterReq memberRegisterReq
    ) {
        Integer memberId = Integer.valueOf(authentication.getName());
        return ResponseEntity.ok().body(memberService.register(memberId, memberRegisterReq));
    }

    @GetMapping("/info")
    @Operation(summary = "회원 정보 확인",
            description = des_header_token + des_output + des_SimpleMemberInfoRes)
    public ResponseEntity<SimpleMemberInfoRes> getSimpleInfo(
            Authentication authentication
    ) {
        Integer memberId = Integer.valueOf(authentication.getName());
        return ResponseEntity.ok().body(memberService.getSimpleMemberInfo(memberId));
    }
}
