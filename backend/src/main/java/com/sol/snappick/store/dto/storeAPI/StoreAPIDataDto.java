package com.sol.snappick.store.dto.storeAPI;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreAPIDataDto {
    private int storeId;
    private String name;
    private int brandId;
    private int categoryId;
    private String mainBrand;
    private String title;
    private String address;
    private String detailAddress;
    private boolean preRegister;
    private String preRegisterLink;
    private String hashtag;
    private String thumbnails;
    private Double latitude;
    private Double longitude;
    private int views;
    private String searchItems;
    private String startDate;
    private String endDate;
    private boolean isExpired;
    private String workingTime;
    private String status;
    private int totalStars;
    private int totalComments;
    private int totalFavorites;
    private String approval;
    private String previewId;
    private int totalUserLike;
    private StoreAPIDetailDto storeDetail;
    private List<StoreAPIImageDto> storeImage;
//    private List<String> hashtagList;
//    private List<String> reservation;
}
