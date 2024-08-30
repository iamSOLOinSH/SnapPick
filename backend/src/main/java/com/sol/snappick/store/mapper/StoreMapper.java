package com.sol.snappick.store.mapper;

import com.sol.snappick.store.dto.StoreCreateReq;
import com.sol.snappick.store.dto.StoreImageDto;
import com.sol.snappick.store.dto.StoreRes;
import com.sol.snappick.store.dto.StoreRunningTimeDto;
import com.sol.snappick.store.dto.StoreUpdateReq;
import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.entity.StoreImage;
import com.sol.snappick.store.entity.StoreRunningTime;
import com.sol.snappick.store.entity.StoreTag;
import com.sol.snappick.store.entity.StoreVisit;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface StoreMapper {

	@Mapping(target = "images", ignore = true)
	@Mapping(target = "tags", ignore = true)
	@Mapping(target = "runningTimes", ignore = true)
	Store toEntity(StoreCreateReq dto);

    @Mapping(source = "tags", target = "tags", qualifiedByName = "entityTagsToString")
    @Mapping(source = "images", target = "images", qualifiedByName = "mapImageDtos")
    @Mapping(source = "runningTimes", target = "runningTimes", qualifiedByName = "mapRunningTimeDtos")
    @Mapping(source = "visits", target = "visitCount", qualifiedByName = "mapVisitCount")
    @Mapping(source = "member.id", target = "sellerId")
    StoreRes toDto(Store entity);

    List<Store> toEntityList(List<StoreCreateReq> dtoList);

    List<StoreRes> toDtoList(List<Store> entityList);

    // StoreTag -> String
    @Named("entityTagsToString")
    default List<String> entityTagsToString(List<StoreTag> tags) {
        return tags.stream()
                   .map(StoreTag::getTag)
                   .toList();
    }

    // StoreImageDto -> StoreImage 매핑
    @Named("mapImages")
    default List<StoreImage> mapImages(List<StoreImageDto> imageDtos) {
        return imageDtos.stream()
                        .map(dto -> StoreImage.builder()
                                              .originImageUrl(dto.getOriginImageUrl())
                                              .thumbnailImageUrl(dto.getThumbnailImageUrl())
                                              .build())
                        .toList();
    }

    // storeImage -> storeImageDto
    @Named("mapImageDtos")
    default List<StoreImageDto> mapImageDtos(List<StoreImage> images) {
        if(images == null) {
            return null;
        }
        return images.stream()
                     .map(image -> StoreImageDto.builder()
                                                .originImageUrl(image.getOriginImageUrl())
                                                .thumbnailImageUrl(image.getThumbnailImageUrl())
                                                .build())
                     .toList();
    }

    // StoreRunningTime -> StoreRunningTimeDto 매핑
    @Named("mapRunningTimeDtos")
    default List<StoreRunningTimeDto> mapRunningTimeDtos(List<StoreRunningTime> runningTimes) {
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        return runningTimes.stream()
                           .map(rt -> StoreRunningTimeDto.builder()
                                                         .dayOfWeek(rt.getDayOfWeek())
                                                         .startTime(rt.getStartTime()
                                                                      .format(timeFormatter))
                                                         .endTime(rt.getEndTime()
                                                                    .format(timeFormatter))
                                                         .build())
                           .toList();
    }

    @Named("mapVisitCount")
    default int mapVisitCount(List<StoreVisit> visits) {
        return visits != null ? visits.size() : 0;
    }

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "images", ignore = true)
    @Mapping(target = "tags", ignore = true)
    @Mapping(target = "runningTimes", ignore = true)
    @Mapping(target = "name", source = "name")
    @Mapping(target = "description", source = "description")
    @Mapping(target = "location", source = "location")
    @Mapping(target = "latitude", source = "latitude")
    @Mapping(target = "longitude", source = "longitude")
    @Mapping(target = "operateStartAt", source = "operateStartAt")
    @Mapping(target = "operateEndAt", source = "operateEndAt")
    @Mapping(target = "status", source = "status")
    void updateEntityFromDto(
            StoreUpdateReq storeUpdateReq,
            @MappingTarget Store store
    );
}
