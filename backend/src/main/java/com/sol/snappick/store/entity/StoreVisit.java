package com.sol.snappick.store.entity;

import com.sol.snappick.member.entity.Member;
import com.sol.snappick.product.entity.Cart;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreVisit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private LocalDateTime visitedAt;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Member customer;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @OneToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Builder
    public StoreVisit(LocalDateTime visitedAt, Member customer, Store store, Cart cart) {
        this.visitedAt = visitedAt;
        this.customer = customer;
        this.store = store;
        this.cart = cart;
    }
}
