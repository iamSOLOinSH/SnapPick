import { useState, useEffect } from "react";
import { useNavigate, useSearchParams } from "react-router-dom";
import { Layout } from "../components/common/Layout";
import { Tag } from "../components/common/Tag";
import { SearchBar } from "../components/common/SearchBar";
import { Card } from "../components/common/Card";
import { Store } from "../types/store";
import { getStores } from "../utils/api/store";

type SortOption = {
  label: string;
  value: string;
};

const sortOptions: SortOption[] = [
  { label: "조회수 순", value: "VIEWS" },
  { label: "최신 등록 순", value: "RECENT" },
  { label: "운영 마감 임박 순", value: "CLOSING_SOON" },
];

const StoreSearch = () => {
  const navigate = useNavigate();

  const [searchParams, setSearchParams] = useSearchParams();
  const [isSearched, setIsSearched] = useState(false);

  const [selectedSort, setSelectedSort] = useState<string>("VIEWS");
  const [searchText, setSearchText] = useState("");
  const [stores, setStores] = useState<Store[]>([]);

  useEffect(() => {
    const query = searchParams.get("query") || "";
    const sort = searchParams.get("sort") || "VIEWS";
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

    handleGetStores();
  };

  const handleSearchChange = (value: string) => {
    setSearchText(value);
  };

  const handleStoreDetail = (id: number) => {
    navigate(`/store/detail/${id}`);
  };

  const handleGetStores = async () => {
    try {
      const storesData = await getStores(searchText, 2000, 0, selectedSort);
      setStores(storesData.data);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    handleGetStores();
  }, [selectedSort]);

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
                  title={store?.name}
                  subtitle={store?.location}
                  imageSrc={store?.images[0]?.originImageUrl}
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
