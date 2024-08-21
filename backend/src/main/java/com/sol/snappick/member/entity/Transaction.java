package com.sol.snappick.member.entity;


import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column
    private Long beforeAmount;

    @Column
    private Long afterAmount;

    @Column
    private Long variation;

    @Column
    private LocalDateTime transactedAt;

    protected Transaction() {
    }
}
