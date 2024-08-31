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
import { FaMapMarkerAlt } from "react-icons/fa";

const StoreControl = () => {
  const navigate = useNavigate();
  const { storeVisitHistory, getVisitHistory } = useBoundStore((state) => ({
    storeVisitHistory: state.storeVisitHistory,
    getVisitHistory: state.getVisitHistory,
  }));
  const [searchQuery, setSearchQuery] = useState<string>("");
  const [filteredHistory, setFilteredHistory] =
    useState<Store[]>(storeVisitHistory);
  const [selectedStore, setSelectedStore] = useState<number | null>(null);

  useEffect(() => {
    getVisitHistory();
  }, []);

  useEffect(() => {
    if (searchQuery === "") {
      setFilteredHistory(storeVisitHistory);
    }
  }, [searchQuery, storeVisitHistory]);

  const handleSearch = () => {
    const query = searchQuery.toLowerCase();
    const filtered = storeVisitHistory.filter(
      (item) =>
        item.name.toLowerCase().includes(query) ||
        item.location.toLowerCase().includes(query),
    );
    setFilteredHistory(filtered);
  };

  const handleCardClick = (storeId: number) => {
    setSelectedStore(selectedStore === storeId ? null : storeId);
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
              {/* 카드 디자인 시작 */}
              <div
                className={`relative z-0 flex flex-col space-y-2 rounded-lg border border-primary px-6 py-4 text-primary`}
                onClick={() => handleCardClick(item.id)}
              >
                {/* 카드 메인 */}
                <div className="flex justify-between">
                  <h3 className={`text-lg font-bold text-primary`}>
                    {item?.name}
                  </h3>
                  <span className={`flex-shrink-0 text-sm text-black`}>
                    {formatDate(item?.operateStartAt, true)}
                  </span>
                </div>
                <hr className="my-2 border-dashed" />
                <div className="flex items-center justify-between">
                  <div className="flex items-center space-x-2">
                    <FaMapMarkerAlt />
                    <p className="text-sm">{item?.location}</p>
                  </div>
                </div>
              </div>
              {/* 카드 디자인 끝 */}
              {selectedStore === item.id && (
                <div className="mt-2 flex justify-center space-x-4">
                  <button
                    className="rounded bg-primary px-4 py-2 text-white hover:bg-primary"
                    onClick={() => navigate(`/stock/${item.id}`)}
                  >
                    재고 관리
                  </button>
                  <button
                    className="rounded bg-green px-4 py-2 text-white hover:bg-green"
                    onClick={() => navigate(`/store/reception/${item.id}`)}
                  >
                    수령 현황
                  </button>
                </div>
              )}
            </li>
          ))}
        </ul>
      </div>
    </Layout>
  );
};

export default StoreControl;
