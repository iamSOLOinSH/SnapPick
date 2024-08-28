package com.sol.snappick.store.repository;

import static com.sol.snappick.store.entity.QStore.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.hibernate.query.sqm.PathElementException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sol.snappick.store.dto.StoreSearchConditionDto;
import com.sol.snappick.store.dto.StoreSearchReq;
import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.entity.StoreStatus;
import com.sol.snappick.store.exception.InvalidAttributeException;

import lombok.AllArgsConstructor;

@Repository
@AllArgsConstructor
public class StoreCustomRepositoryImpl implements StoreCustomRepository {
	private final JPAQueryFactory queryFactory;

	/**
	 * sellerId로 필터링
	 * @param memberId
	 * @return
	 */
	@Override
	public List<Store> findBySellerId(Integer memberId) {

		return queryFactory.selectFrom(store)
						   .where(store.member.id.eq(memberId))
						   .fetch();
	}

	@Override
	public List<Store> findByConditions(
			StoreSearchReq dto,
			Pageable pageable
	) {
		// 동적 쿼리 시작
		BooleanBuilder predicate = new BooleanBuilder();
		PathBuilder<Store> entityPath = new PathBuilder<>(Store.class,
														  "store");

		// 각 조건 별로 쿼리 조건 추가
		for(StoreSearchConditionDto condition : dto.getConditions()) {
			try {
				// 필드 유효성 검사
				if(condition.getField() != null && condition.getValues() != null && !condition.getValues()
																							  .isEmpty()) {
					switch(condition.getField()) {
						case "name":
							// name 인 경우 like 연산 적용
							for(String value : condition.getValues()) { // 아마 value 는 한 개 들어올 거라 예상
								predicate.and(store.name.like("%" + value + "%"));
							}
							break;
						case "tag":
							// tag일 경우 stores.tags에서 검색
							for(String value : condition.getValues()) {
								predicate.and(store.tags.any().tag.eq(value));
							}
							break;
						default:
							// IN 쿼리
							BooleanExpression inExpression = entityPath.getString(condition.getField())
																	   .in(condition.getValues());
							predicate.and(inExpression);
							break;
					}
				}
			} catch(InvalidDataAccessApiUsageException | PathElementException ex) {
				throw new InvalidAttributeException("잘못된 속성 이름이 사용되었습니다 : " + condition.getField());
			}
		}

		// 만약 closing_soon 조건일 경우 마감일이 현재 날짜 이후여야함
		if(dto.getSortType() == StoreSearchReq.SortType.CLOSING_SOON) {
			// 임시 종료, 종료인 스토어는 제외
			List<StoreStatus> excludedStatuses = new ArrayList<>();
			excludedStatuses.add(StoreStatus.TEMPORARILY_CLOSED);
			excludedStatuses.add(StoreStatus.CLOSED);

			predicate.and(store.status.notIn(excludedStatuses));
			predicate.and(store.operateEndAt.after(LocalDate.now()));
		}

		// OrderBy 뺀 쿼리
		JPQLQuery<Store> query = queryFactory.selectFrom(store)
											 .where(predicate);

		if(dto.getSortType() == StoreSearchReq.SortType.CLOSING_SOON) { // 종료일 임박 순
			query.orderBy(store.operateEndAt.asc());
		} else if(dto.getSortType() == StoreSearchReq.SortType.RECENT) { // 최신 순
			query.orderBy(store.createdAt.desc());
		} else if(dto.getSortType() == StoreSearchReq.SortType.VIEWS) { // 조회수 순
			query.orderBy(store.viewCount.desc());
		} else { // 기본 정렬 기준 : 최근 순
			query.orderBy(store.createdAt.asc());
		}

		return query.offset(pageable.getOffset())
					.limit(pageable.getPageSize())
					.fetch();
	}

	/**
	 * 종료 상태인 스토어는 제외
	 * @return
	 */
	@Override
	@Transactional(readOnly = true)
	public List<Store> findWithoutClosed() {
		BooleanBuilder predicate = new BooleanBuilder();

		List<StoreStatus> excludedStatuses = new ArrayList<>();
		excludedStatuses.add(StoreStatus.TEMPORARILY_CLOSED);
		excludedStatuses.add(StoreStatus.CLOSED);
		predicate.and(store.status.notIn(excludedStatuses)
								  .or(store.status.isNull()));

		return queryFactory.selectFrom(store)
						   .where(predicate)
						   .fetch();
	}

	/**
	 * uuid로 id 찾기
	 * @param storeUUID
	 * @return
	 */
	@Override
	public Integer findByUUID(UUID storeUUID) {
		return queryFactory.select(store.id)
						   .from(store)
						   .where(store.uuid.eq(storeUUID))
						   .fetchOne();
	}
}
