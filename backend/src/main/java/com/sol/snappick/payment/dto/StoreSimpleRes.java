package com.sol.snappick.payment.dto;

import com.sol.snappick.store.dto.StoreImageDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class StoreSimpleRes {

    @Schema(description = "스토어 id")
    private Integer id;
    @Schema(description = "스토어 이름")
    private String name;
    @Schema(description = "스토어 위치")
    private String location;
    @Schema(description = "스토어 썸네일")
    private List<String> images;

}
