package com.sol.snappick.member.dto;

import com.sol.snappick.member.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class DetailMemberInfoRes {

    private Integer id;
    private String email;
    private String name;
    private String phoneNumber;
    private String role;
    private String imagerUrl;
    private String userKey;
    private String accountNumber;

    public static DetailMemberInfoRes fromEntity(Member member) {
        return DetailMemberInfoRes.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .phoneNumber(member.getPhoneNumber())
                .role(member.getRole() != null ? member.getRole().name() : null)
                .imagerUrl(member.getProfileImageUrl())
                .userKey(member.getUserKey())
                .accountNumber(member.getAccountNumber())
                .build();
    }
}
