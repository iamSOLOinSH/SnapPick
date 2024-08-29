package com.sol.snappick.payment.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CustomerRes {

    @Schema(description = "회원 id")
    private int memberId;
    @Schema(description = "회원 이름")
    private String name;
    @Schema(description = "회원 전화번호")
    private String phoneNumber;

}
