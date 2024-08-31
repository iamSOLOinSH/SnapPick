import React, { useState, useEffect } from "react";
import { useBoundStore } from "../store/store";
import { Layout } from "../components/common/Layout";
import { BackButton } from "../components/common/BackButton";
import { SearchBar } from "../components/common/SearchBar";
import { TicketCard } from "../components/common/TicketCard";
import { isToday, formatDate } from "../utils/Date";
import { StoreData } from "../types/store";

const VisitHistory = () => {
  const { storeDataVisitHistory, getVisitHistory } = useBoundStore((state) => ({
    storeDataVisitHistory: state.storeDataVisitHistory,
    getVisitHistory: state.getVisitHistory,
  }));
  const [searchQuery, setSearchQuery] = useState<string>("");
  const [filteredHistory, setFilteredHistory] = useState<StoreData[]>(
    storeDataVisitHistory,
  );

  useEffect(() => {
    if (searchQuery === "") {
      setFilteredHistory(storeDataVisitHistory);
    }
  }, [searchQuery, storeDataVisitHistory]);

  const handleSearch = () => {
    const query = searchQuery.toLowerCase();
    const filtered = storeDataVisitHistory.filter(
      (item) =>
        item?.storeDetailDto?.name.toLowerCase().includes(query) ||
        item?.storeDetailDto?.location.toLowerCase().includes(query),
    );
    setFilteredHistory(filtered);
  };

  useEffect(() => {
    getVisitHistory();
  }, [getVisitHistory]);

  return (
    <Layout className="bg-white">
      <div className="mb-8 mt-12 flex items-center justify-between">
        <BackButton />
        <h2 className="flex-grow text-center text-2xl font-bold">결제 내역</h2>
        <div className="w-8" />
      </div>
      <div className="mx-2 mb-2">
        <SearchBar
          value={searchQuery}
          onChange={(value) => setSearchQuery(value)}
          onSearch={handleSearch}
          placeholder="방문한 위치/스토어를 검색해보세요"
        />
      </div>
      <div className="mx-2 mt-4 min-h-[68vh]">
        <ul className="flex flex-col gap-2">
          {filteredHistory.map((item, index) => (
            <li
              key={index}
              className="animate-fadeInSlideUp"
              style={{ animationDelay: `${index * 0.12}s` }}
            >
              <TicketCard
                title={item?.storeDetailDto?.name}
                date={formatDate(item?.storeVisitDto?.visitedAt, true)}
                location={item?.storeDetailDto?.location}
                price={item?.cartPurchasedDto?.purchasedAmount}
                buttonText="상세 보기"
                isActive={isToday(item?.storeVisitDto?.visitedAt)}
              />
            </li>
          ))}
        </ul>
      </div>
    </Layout>
  );
};

export default VisitHistory;
