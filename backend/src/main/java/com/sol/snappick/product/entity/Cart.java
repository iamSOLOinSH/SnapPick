package com.sol.snappick.product.entity;

import java.util.List;

import com.sol.snappick.global.BaseEntity;
import com.sol.snappick.member.entity.Member;
import com.sol.snappick.store.entity.Store;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;

@Entity
@Getter
public class Cart extends BaseEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne
	@JoinColumn(name = "store_id")
	private Store store;

	@ManyToOne
	@JoinColumn(name = "customer_id")
	private Member customer;

	// 아이템 목록
	@OneToMany(mappedBy = "cart", cascade = CascadeType.REMOVE)
	private List<CartItem> items;

	@Column
	private Boolean isPaid;

	protected Cart() {
	}

	@Builder
	public Cart(
			Store store,
			Member customer,
			List<CartItem> items,
			Boolean isPaid
	) {
		this.store = store;
		this.customer = customer;
		this.items = items;
		this.isPaid = isPaid;
	}
}
