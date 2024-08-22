package com.sol.snappick.member.entity;

import com.sol.snappick.global.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;


@Entity
@Getter
//@Setter
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String email;

    @Column(length = 10)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column
    private Role role;

    @Column(length = 4)
    private String pinCode;

    @Column(length = 15)
    private String phoneNumber;

    @Column
    private String accountNumber;

    @Column(name = "business_registration_number", length = 20)
    private String businessNumber;

    @Column(length = 36)
    private String userKey;

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

}
