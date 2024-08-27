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
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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
    @OnDelete(action = OnDeleteAction.CASCADE)
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
