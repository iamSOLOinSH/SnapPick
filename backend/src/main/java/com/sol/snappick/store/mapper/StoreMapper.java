package com.sol.snappick.store.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sol.snappick.store.dto.StoreCreateReq;
import com.sol.snappick.store.dto.StoreImageDto;
import com.sol.snappick.store.dto.StoreRes;
import com.sol.snappick.store.dto.StoreRunningTimeDto;
import com.sol.snappick.store.dto.StoreUpdateReq;
import com.sol.snappick.store.dto.storeAPI.StoreAPIDataDto;
import com.sol.snappick.store.dto.storeAPI.StoreAPIImageDto;
import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.entity.StoreImage;
import com.sol.snappick.store.entity.StoreRunningTime;
import com.sol.snappick.store.entity.StoreTag;
import com.sol.snappick.store.entity.StoreVisit;
import com.sol.snappick.util.DayUtil;
import com.sol.snappick.util.HashtagUtil;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    @Mapping(target = "member", ignore = true)
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
        if (images == null) {
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

    /**
     * storeAPI 데이터 -> 우리 서비스 데이터 생성 DTO
     *
     * @param dto StoreAPIDataDto
     * @return StoreCreateReq
     * @throws JsonProcessingException
     */
    default StoreCreateReq apiDataToStoreCreateReq(StoreAPIDataDto dto)
        throws JsonProcessingException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // string hashtag -> tag list
        List<String> hashtagList = HashtagUtil.convertToList(dto.getHashtag());
        // image
        List<StoreImageDto> images = new ArrayList<>();
        // runningTime
        List<StoreRunningTimeDto> runningTimes = new ArrayList<>();

        if (dto.getStoreImage() != null) {
            // image 바꿔서 넣기
            for (StoreAPIImageDto image : dto.getStoreImage()) {
                images.add(StoreImageDto.builder()
                                        .originImageUrl(image.getUrl())
                                        .build());
            }
        }

        // runningTime 바꿔서 넣기
        ObjectMapper objectMapper = new ObjectMapper();

        if (dto.getWorkingTime() != null) {
            // JSON 문자열 -> List<Map<String, Object>> 로 변환
            List<Map<String, Object>> runningTimeList = objectMapper.readValue(dto.getWorkingTime(),
                                                                               new TypeReference<List<Map<String, Object>>>() {}
            );

            // StoreRunningTimeDto 리스트로 변환
            for (Map<String, Object> map : runningTimeList) {
                // day 가 없으면 넘기기
                if (!map.containsKey("day")) {
                    continue;
                }
                String day = (String) map.get("day");
                DayOfWeek dayOfWeek = DayUtil.mapDayOfWeek(day);

                String startTime = (String) map.get("startDate");
                String endTime = (String) map.get("endDate");

                runningTimes.add(StoreRunningTimeDto.builder()
                                                    .dayOfWeek(dayOfWeek)
                                                    .startTime(startTime)
                                                    .endTime(endTime)
                                                    .build());
            }
        }

        return StoreCreateReq.builder()
                             .name(dto.getName())
                             .description(dto.getStoreDetail()
                                             .getContents())
                             .viewCount(dto.getViews())
                             .location(dto.getAddress())
                             .latitude(dto.getLatitude())
                             .longitude(dto.getLongitude())
                             .operateStartAt(LocalDate.parse(dto.getStartDate()
                                                                .substring(0,
                                                                           10
                                                                )))  // Converting to LocalDate
                             .operateEndAt(LocalDate.parse(dto.getEndDate()
                                                              .substring(0, 10)))
                             .sellerId(null)
                             .tags(hashtagList)
                             .images(images)
                             .runningTimes(runningTimes)
                             .build();
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
