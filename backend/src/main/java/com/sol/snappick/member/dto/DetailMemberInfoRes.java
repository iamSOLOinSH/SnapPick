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
    private String role;
    private String imagerUrl;
    private String userKey;
    // 이후 항목 추가

    public static DetailMemberInfoRes fromEntity(Member member) {
        return DetailMemberInfoRes.builder()
                .id(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .role(member.getRole() != null ? member.getRole().name() : null)
                .imagerUrl(member.getProfileImageUrl())
                .userKey(member.getUserKey())
                .build();
    }
}
