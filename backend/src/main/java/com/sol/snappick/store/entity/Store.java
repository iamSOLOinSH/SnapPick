package com.sol.snappick.store.entity;

import com.sol.snappick.global.BaseEntity;
import com.sol.snappick.member.entity.Member;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
    @Setter
    private String name;

    @Column(columnDefinition = "TEXT")
    @Setter
    private String description;

    @Column
    @Setter
    private String location;

    @Column
    @Setter
    private Double latitude;

    @Column
    @Setter
    private Double longitude;

    @Column
    @Setter
    private LocalDate operateStartAt;

    @Column
    @Setter
    private LocalDate operateEndAt;

    @Column(nullable = false)
    private Integer viewCount;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column
    @Setter
    private StoreStatus status;

    // 태그 목록
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter
    private List<StoreTag> tags;

    // 스토어 이미지
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter
    private List<StoreImage> images;

    // 운영 시간
    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    @Setter
    private List<StoreRunningTime> runningTimes;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StoreVisit> visits;

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
        Integer viewCount,
        List<StoreTag> tags,
        List<StoreImage> images,
        List<StoreRunningTime> runningTimes,
        List<StoreVisit> visits
    ) {
        this.name = name;
        this.description = description;
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.operateStartAt = operateStartAt;
        this.operateEndAt = operateEndAt;
        this.member = member;
        this.viewCount = viewCount;
        this.tags = tags;
        this.images = images;
        this.runningTimes = runningTimes;
        this.visits = visits;
    }

    // visits 리스트의 크기를 반환하는 메서드
    public int getVisitCount() {
        return (visits != null) ? visits.size() : 0;
    }

    public void incrementViewCount() {
        this.viewCount++;
    }

    // 상태를 업데이트하는 메서드
    public void updateStatus() {
        // 현재 상태가 TEMPORARILY_CLOSED라면 상태를 자동 업데이트하지 않음
        if (this.status == StoreStatus.TEMPORARILY_CLOSED && this.status == StoreStatus.CLOSED) {
            return;
        }

        LocalDate today = LocalDate.now();

        if (operateStartAt != null && operateEndAt != null) {
            if (today.isBefore(operateStartAt)) {
                this.status = StoreStatus.PREPARING; // 운영 준비중
            } else if (today.isAfter(operateEndAt)) {
                this.status = StoreStatus.CLOSED; // 운영 마감
            } else {
                this.status = StoreStatus.OPERATING; // 운영중
            }
        }
    }
}
