package com.sol.snappick.product.entity;

import com.sol.snappick.global.BaseEntity;
import com.sol.snappick.member.entity.Member;
import com.sol.snappick.store.entity.Store;
import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class Cart extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store storeId;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member customerId;

    // 아이템 목록
    @OneToMany(mappedBy = "cart", cascade = CascadeType.REMOVE)
    private List<CartItem> items;

    @Column
    private Boolean isPaid;

    protected Cart() {
    }
}
