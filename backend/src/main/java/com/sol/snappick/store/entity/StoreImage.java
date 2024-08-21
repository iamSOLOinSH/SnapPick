package com.sol.snappick.store.entity;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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

    @Column
    private String originImageUrl;

    @Column
    private String thumbnailImageUrl;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @Builder
    public StoreImage(String originImageUrl, String thumbnailImageUrl, Store store) {
        this.originImageUrl = originImageUrl;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.store = store;
    }
}
