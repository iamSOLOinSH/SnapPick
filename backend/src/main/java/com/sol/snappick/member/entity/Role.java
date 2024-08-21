package com.sol.snappick.member.entity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 유형")
public enum Role {
    판매자, 구매자
}
