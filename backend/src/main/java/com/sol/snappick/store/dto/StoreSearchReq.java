package com.sol.snappick.store.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema (description = "스토어 검색 요청 DTO")
public class StoreSearchReq {
	List<StoreSearchConditionDto> conditions;

	@Schema (description = "한 페이지에 표시할 항목 수", example = "10")
	Integer size;

	@Schema (description = "페이지 번호 (0부터 시작)", example = "0")
	Integer page;

	@Schema (description = "정렬 타입 (조회순: VIEWS, 최근 등록: RECENT, 운영 마감 임박: CLOSING_SOON)")
	SortType sortType;

	public enum SortType {
		VIEWS, RECENT, CLOSING_SOON
	}
}
