import { useState, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { Layout } from "../components/common/Layout";
import { Tag } from "../components/common/Tag";
import { SearchBar } from "../components/common/SearchBar";
import { getStores } from "../mocks/store";
import { Card } from "../components/common/Card";

type SortOption = {
  label: string;
  value: string;
};

const sortOptions: SortOption[] = [
  { label: "조회수 순", value: "views" },
  { label: "최신 등록 순", value: "latest" },
  { label: "운영 마감 임박 순", value: "soon" },
];

const StoreSearch = () => {
  const navigate = useNavigate();

  const [searchParams, setSearchParams] = useSearchParams();
  const [isSearched, setIsSearched] = useState(false);

  const [selectedSort, setSelectedSort] = useState<string>("views");
  const [searchText, setSearchText] = useState("");
  const stores = getStores();

  useEffect(() => {
    const query = searchParams.get("query") || "";
    const sort = searchParams.get("sort") || "views";
    console.log(query);
    setSearchText(query);
    setSelectedSort(sort);
    setIsSearched(!!query);
  }, [searchParams]);

  const updateURL = (query: string, sort: string) => {
    const newSearchParams = new URLSearchParams();
    if (query) newSearchParams.set("query", query);
    if (sort) newSearchParams.set("sort", sort);
    setSearchParams(newSearchParams);
  };

  const handleSortChange = (value: string) => {
    setSelectedSort(value);
    updateURL(searchText, value);
  };

  const handleSearch = () => {
    setIsSearched(true);
    updateURL(searchText, selectedSort);
  };

  const handleSearchChange = (value: string) => {
    setSearchText(value);
  };

  const handleStoreDetail = (id: string) => {
    navigate(`/store/detail/${id}`);
  };

  return (
    <Layout>
      <div className="flex min-h-screen flex-col">
        <div className="mb-4">
          <SearchBar
            placeholder="스토어를 검색해보세요"
            value={searchText}
            onChange={handleSearchChange}
            onSearch={handleSearch}
          />
        </div>
        <div className="mb-4 ml-2">
          {sortOptions.map((option) => (
            <Tag
              className="mr-1"
              key={option.value}
              content={option.label}
              value={option.value}
              isSelectable={true}
              isSelected={selectedSort === option.value}
              onClick={handleSortChange}
            />
          ))}
        </div>
        {/* 검색 결과 */}
        {isSearched && (
          <div>
            {stores.map((store) => (
              <div
                className="cursor-pointer"
                onClick={() => handleStoreDetail(store.id)}
              >
                <Card
                  key={store.id}
                  variant="store"
                  title={store.title}
                  subtitle={store.location}
                  imageSrc={store.imageSrc}
                  description={store.description}
                />
              </div>
            ))}
          </div>
        )}
      </div>
    </Layout>
  );
};

export default StoreSearch;
