import React, { useState, useEffect } from "react";
import { useBoundStore } from "../store/store";
import { Layout } from "../components/common/Layout";
import { BackButton } from "../components/common/BackButton";
import { SearchBar } from "../components/common/SearchBar";
import { TicketCard } from "../components/common/TicketCard";
import { isToday, formatDate } from "../utils/Date";

interface VisitHistoryItem {
  name: string;
  location: string;
  price: number;
  visitedAt: string;
}

const VisitHistory = () => {
  const { visitHistory } = useBoundStore((state) => ({
    visitHistory: state.visitHistory,
  }));
  const [searchQuery, setSearchQuery] = useState<string>("");
  const [filteredHistory, setFilteredHistory] =
    useState<VisitHistoryItem[]>(visitHistory);

  useEffect(() => {
    if (searchQuery === "") {
      setFilteredHistory(visitHistory);
    }
  }, [searchQuery, visitHistory]);

  const handleSearch = () => {
    const query = searchQuery.toLowerCase();
    const filtered = visitHistory.filter(
      (item) =>
        item.name.toLowerCase().includes(query) ||
        item.location.toLowerCase().includes(query),
    );
    setFilteredHistory(filtered);
  };

  return (
    <Layout>
      <div className="mb-8 mt-12 flex items-center justify-between">
        <BackButton />
        <h2 className="flex-grow text-center text-2xl font-bold">방문 기록</h2>
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
                title={item.name}
                date={formatDate(item.visitedAt, true)}
                location={item.location}
                price={item.price}
                buttonText="상세 보기"
                isActive={isToday(item.visitedAt)}
              />
            </li>
          ))}
        </ul>
      </div>
    </Layout>
  );
};

export default VisitHistory;
