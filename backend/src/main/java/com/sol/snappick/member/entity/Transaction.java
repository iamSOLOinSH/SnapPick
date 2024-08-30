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

    @Column(name = "to_account_no")
    private String toAccountNo;

    @Column(name = "from_account_no")
    private String fromAccountNo;

    @Enumerated(EnumType.STRING)
    @Column
    private TransactionType type;

    @Column
    private String transactionUniqueNo;

    @Column
    private Long variation;

    @Column
    private LocalDateTime transactedAt;

    @Column
    private String summary;

    protected Transaction() {
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

}
