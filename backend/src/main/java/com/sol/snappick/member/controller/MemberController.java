package com.sol.snappick.member.controller;

import com.sol.snappick.member.dto.DetailMemberInfoRes;
import com.sol.snappick.member.dto.MemberRegisterReq;
import com.sol.snappick.member.dto.SimpleMemberInfoRes;
import com.sol.snappick.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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
    @Operation(summary = "회원정보 확인",
            description = des_header_token + des_output + des_SimpleMemberInfoRes)
    public ResponseEntity<SimpleMemberInfoRes> getSimpleInfo(
            Authentication authentication
    ) {
        Integer memberId = Integer.valueOf(authentication.getName());
        return ResponseEntity.ok().body(memberService.getSimpleMemberInfo(memberId));
    }


    @GetMapping("/pincode")
    @Operation(summary = "(개발용) 핀코드 일치여부 확인",
            description = des_header_token)
    public ResponseEntity<Boolean> isCorrectPin(
            Authentication authentication,
            @RequestParam(name = "pin_code") String pinCode
    ) {
        Integer memberId = Integer.valueOf(authentication.getName());
        return ResponseEntity.ok().body(memberService.isCorrectPin(memberId, pinCode));
    }

    @PostMapping("/pincode")
    @Operation(summary = "핀코드 재설정",
            description = "새로운 핀코드를 저장합니다.<br/>" + des_header_token)
    public ResponseEntity<Void> resetPin(
            Authentication authentication,
            @RequestParam(name = "pin_code") String pinCode
    ) {
        Integer memberId = Integer.valueOf(authentication.getName());
        memberService.resetPin(memberId, pinCode);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/dev/info")
    @Operation(summary = "(개발용) 회원정보 확인",
            description = "액세스 토큰, 또는 이메일, 또는 id를 통해 회원정보를 확인합니다")
    public ResponseEntity<List<DetailMemberInfoRes>> getAllInfo(
            @RequestParam(value = "token", required = false) String token,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "memberId", required = false) String memberId) {

        List<DetailMemberInfoRes> responseData = new ArrayList<>();
        if (token != null) {
            responseData = memberService.getMemberInfo(token, "token");
        } else if (email != null) {
            responseData = memberService.getMemberInfo(email, "email");
        } else if (memberId != null) {
            responseData = memberService.getMemberInfo(memberId, "id");
        } else {
            responseData = memberService.getMemberInfo(null, "all");
        }
        return ResponseEntity.ok().body(responseData);
    }
}
