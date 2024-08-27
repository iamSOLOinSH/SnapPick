package com.sol.snappick.store.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema (description = "스토어 검색 조건 DTO")
public class StoreSearchConditionDto {
	@Schema (description = "검색할 컬럼 이름", example = "name")
	@NotNull
	String field;

	@Schema (description = "검색할 값 리스트", example = "[\"로스트아크\"]")
	@NotNull
	@Size (min = 1)
	List<String> values;
}
