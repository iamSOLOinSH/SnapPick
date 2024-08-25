package com.sol.snappick.store.repository;

import static com.sol.snappick.store.entity.QStore.*;

import java.util.List;

import org.hibernate.query.sqm.PathElementException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sol.snappick.store.dto.StoreSearchConditionDto;
import com.sol.snappick.store.dto.StoreSearchReq;
import com.sol.snappick.store.entity.Store;
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
	public List<Store> findBySellerId (Integer memberId) {

		return queryFactory.selectFrom(store)
						   .where(store.member.id.eq(memberId))
						   .fetch();
	}

	@Override
	public List<Store> findByConditions (
		StoreSearchReq dto,
		Pageable pageable
	) {
		// 동적 쿼리 시작
		BooleanBuilder predicate = new BooleanBuilder();
		PathBuilder<Store> entityPath = new PathBuilder<>(Store.class,
														  "store");

		// 각 조건 별로 쿼리 조건 추가
		for (StoreSearchConditionDto condition : dto.getConditions()) {
			try {
				// 필드 유효성 검사
				if ( condition.getField() != null && condition.getValues() != null && !condition.getValues()
																								.isEmpty() ) {

					// IN 쿼리
					BooleanExpression inExpression = entityPath.getString(condition.getField())
															   .in(condition.getValues());
					predicate.and(inExpression);

				}
			} catch (InvalidDataAccessApiUsageException | PathElementException ex) {
				throw new InvalidAttributeException("잘못된 속성 이름이 사용되었습니다 : " + condition.getField());
			}
		}
		return queryFactory.selectFrom(store)
						   .where(predicate)
						   .offset(pageable.getPageNumber())
						   .limit(pageable.getPageSize())
						   .fetch();
	}
}
