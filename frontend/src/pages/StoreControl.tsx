import React, { useState, useEffect } from "react";
import { useBoundStore } from "../store/store";
import { Layout } from "../components/common/Layout";
import { BackButton } from "../components/common/BackButton";
import { SearchBar } from "../components/common/SearchBar";
import { TicketCard } from "../components/common/TicketCard";
import { isToday, formatDate } from "../utils/Date";
import { HiPlus } from "react-icons/hi";
import { useNavigate } from "react-router";
import { Store } from "../types/store";

const StoreControl = () => {
  const navigate = useNavigate();
  const { visitHistory, getVisitHistory } = useBoundStore((state) => ({
    visitHistory: state.visitHistory,
    getVisitHistory: state.getVisitHistory,
  }));
  const [searchQuery, setSearchQuery] = useState<string>("");
  const [filteredHistory, setFilteredHistory] = useState<Store[]>(visitHistory);

  useEffect(() => {
    getVisitHistory();
  }, []);

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
    <Layout className="bg-white">
      <div className="mb-8 mt-12 flex w-full items-center justify-between">
        <BackButton />
        <h2 className="text-center text-2xl font-bold">스토어 관리</h2>
        <button
          className="rounded border-2 p-2 hover:bg-base"
          onClick={() => navigate("/store/create")}
        >
          <HiPlus />
        </button>
      </div>
      <div className="mx-2 mb-2">
        <SearchBar
          value={searchQuery}
          onChange={(value) => setSearchQuery(value)}
          onSearch={handleSearch}
          placeholder="스토어를 검색해보세요"
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
                date={formatDate(item.operateStartAt, true)}
                location={item.location}
                buttonText="재고 관리"
                isActive={true}
                onClick={() => navigate(`/stock/${item.id}`)}
              />
            </li>
          ))}
        </ul>
      </div>
    </Layout>
  );
};

export default StoreControl;
