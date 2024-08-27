package com.sol.snappick.member.dto;

import com.sol.snappick.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SimpleMemberInfoRes {

    private String name;
    private String imagerUrl;

    public static SimpleMemberInfoRes fromEntity(Member member) {
        return new SimpleMemberInfoRes(
                member.getName(), member.getProfileImageUrl());
    }
}
