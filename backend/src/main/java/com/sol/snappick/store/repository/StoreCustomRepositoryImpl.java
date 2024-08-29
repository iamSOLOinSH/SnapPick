package com.sol.snappick.store.repository;

import static com.sol.snappick.store.entity.QStore.store;
import static com.sol.snappick.store.entity.QStoreVisit.storeVisit;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sol.snappick.product.dto.CartPurchasedDto;
import com.sol.snappick.product.entity.CartStatus;
import com.sol.snappick.store.dto.StoreSearchReq;
import com.sol.snappick.store.dto.StoreVisitDto;
import com.sol.snappick.store.dto.VisitedStoreDetailDto;
import com.sol.snappick.store.dto.VisitedStoreRes;
import com.sol.snappick.store.entity.Store;
import com.sol.snappick.store.entity.StoreStatus;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@AllArgsConstructor
public class StoreCustomRepositoryImpl implements StoreCustomRepository {

    private final JPAQueryFactory queryFactory;

    /**
     * sellerId로 필터링
     *
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
        PathBuilder<Store> entityPath = new PathBuilder<>(Store.class, "store");

        // 검색 단어에 대해 name, tag 추가
        if (dto.getQuery() != null && !dto.getQuery()
                                          .isEmpty()) {
            predicate.and(store.name.like("%" + dto.getQuery() + "%")
                                    .or(store.tags.any().tag.like("%" + dto.getQuery() + "%")));
        }

        // 만약 closing_soon 조건일 경우 마감일이 현재 날짜 이후여야함
        if (dto.getSortType() == StoreSearchReq.SortType.CLOSING_SOON) {
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

        if (dto.getSortType() == StoreSearchReq.SortType.CLOSING_SOON) { // 종료일 임박 순
            query.orderBy(store.operateEndAt.asc());
        } else if (dto.getSortType() == StoreSearchReq.SortType.RECENT) { // 최신 순
            query.orderBy(store.createdAt.desc());
        } else if (dto.getSortType() == StoreSearchReq.SortType.VIEWS) { // 조회수 순
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
     *
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

    @Override
    public List<VisitedStoreRes> findVisitedStoresByMember(Integer memberId) {
        return queryFactory.select(Projections.constructor(VisitedStoreRes.class,
                                                           Projections.constructor(
                                                               VisitedStoreDetailDto.class,
                                                               store.id, store.name, store.location
                                                           ), Projections.constructor(
                                   StoreVisitDto.class, storeVisit.id, storeVisit.cart.id, storeVisit.visitedAt),
                                                           Projections.constructor(
                                                               CartPurchasedDto.class,
                                                               storeVisit.cart.id,
                                                               storeVisit.cart.transaction.id,
                                                               storeVisit.cart.transaction.variation
                                                           )
                           ))
                           .from(storeVisit)
                           .join(storeVisit.store, store)
                           .where(storeVisit.customer.id.eq(memberId)
                                                        .and(storeVisit.cart.status.eq(
                                                            CartStatus.수령완료)))
                           .fetch();
    }
}
