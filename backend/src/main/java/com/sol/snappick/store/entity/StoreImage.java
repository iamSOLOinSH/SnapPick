package com.sol.snappick.store.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StoreImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(columnDefinition = "TEXT")
    private String originImageUrl;

    @Column(columnDefinition = "TEXT")
    private String thumbnailImageUrl;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder
    public StoreImage(
        String originImageUrl,
        String thumbnailImageUrl,
        Store store
    ) {
        this.originImageUrl = originImageUrl;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.store = store;
    }
}
