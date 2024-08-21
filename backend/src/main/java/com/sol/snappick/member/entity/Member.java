package com.sol.snappick.member.entity;

import com.sol.snappick.global.BaseEntity;
import lombok.Getter;

import javax.persistence.*;

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

    @Column(name = "pin_code", length = 4)
    private String pinCode;

    @Column(name = "phone_number", length = 15)
    private String phoneNumber;

    @Column(name = "account_number")
    private String accountNumber;

    @Column(name = "business_registration_number", length = 20)
    private String businessNumber;

    @Column(name = "user_key", length = 36)
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
