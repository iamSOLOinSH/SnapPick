package com.sol.snappick.store.entity;

import com.sol.snappick.global.BaseEntity;
import com.sol.snappick.member.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.time.LocalDate;
import java.util.List;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column
    private String location;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column
    private LocalDate operateStartAt;

    @Column
    private LocalDate operateEndAt;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Member member;

    // 태그 목록
    @OneToMany(mappedBy = "store")
    @Setter
    private List<StoreTag> tags;

    // 스토어 이미지
    @OneToMany(mappedBy = "store")
    @Setter
    private List<StoreImage> images;

    // 운영 시간
    @OneToMany(mappedBy = "store")
    @Setter
    private List<StoreRunningTime> runningTimes;

    @Builder
    public Store(
        String name,
        String description,
        String location,
        Double latitude,
        Double longitude,
        LocalDate operateStartAt,
        LocalDate operateEndAt,
        Member member,
        List<StoreTag> tags,
        List<StoreImage> images,
        List<StoreRunningTime> runningTimes
    ) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.operateStartAt = operateStartAt;
        this.operateEndAt = operateEndAt;
        this.member = member;
        this.tags = tags;
        this.images = images;
        this.runningTimes = runningTimes;
    }
}
