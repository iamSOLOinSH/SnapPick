package com.sol.snappick.member.entity;

import com.sol.snappick.global.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String email;

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Column(length = 64)
    private String pinCode;

    @Column(length = 15)
    private String phoneNumber;

    @Column
    private String accountNumber;

    @Column(name = "business_registration_number", length = 20)
    private String businessNumber;

    @Column(length = 36)
    private String userKey;

    @Column(name = "is_open_bank", columnDefinition = "boolean default false")
    private Boolean isOpenBank;

    ////////////// 양방향 매핑

    // 방문 기록 목록
    //    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    //    private List<StoreVisite> visits;

    // 판매자가 운영중인 스토어 목록
    //    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    //    private List<Store> stores;

    // 결제 내역과 장바구니
    //    @OneToMany(mappedBy = "member", cascade = CascadeType.REMOVE)
    //    private List<Cart> carts;

    protected Member() {
    }

    @Builder
    public Member(String email, String name, String profileImageUrl) {
        this.email = email;
        this.name = name;
        this.profileImageUrl = profileImageUrl;
        this.isOpenBank = false;
    }

    public void updateProfile(String name, String profileImageUrl) {
        this.name = name;
        this.profileImageUrl = profileImageUrl;
    }


    public void changePinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public void changeAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
