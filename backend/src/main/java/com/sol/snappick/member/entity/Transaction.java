package com.sol.snappick.member.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@Builder
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "account_no")
    private String accountNo;

    @Column(name = "transaction_account_no")
    private String transactionAccountNo;

    @Enumerated(EnumType.STRING)
    @Column
    private TransactionType type;

    @Column
    private String transactionUniqueNo;

    @Column
    private Integer variation;

    @Column
    private LocalDateTime transactedAt;

    protected Transaction() {
    }
}
