package com.sol.snappick.store.entity;

import com.sol.snappick.global.BaseEntity;
import com.sol.snappick.member.entity.Member;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private String location;

    @Column
    private LocalDate operateStartAt;

    @Column
    private LocalDate operateEndAt;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Member member;

    // 태그 목록
    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
    private List<StoreTag> tags;

    // 스토어 이미지
    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
    private List<StoreImage> images;

    // 운영 시간
    @OneToMany(mappedBy = "store", cascade = CascadeType.REMOVE)
    private List<StoreRunningTime> runningTimes;


    @Builder
    public Store(
        String name,
        String description,
        String location,
        LocalDate operateStartAt,
        LocalDate operateEndAt,
        Member member
    ) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.operateStartAt = operateStartAt;
        this.operateEndAt = operateEndAt;
        this.member = member;
    }
}
