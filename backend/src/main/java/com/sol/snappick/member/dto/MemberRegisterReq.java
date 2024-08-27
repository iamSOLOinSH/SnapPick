package com.sol.snappick.member.dto;

import lombok.Getter;

@Getter
public class MemberRegisterReq {

    private int role;
    private String phoneNumber;
    private String businessNumber;
    private String pinCode;


}
