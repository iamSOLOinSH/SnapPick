import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Layout } from "../components/common/Layout";
import { Card } from "../components/common/Card";
import { getStores } from "../mocks/store";
import { IoIosSearch } from "react-icons/io";
import { Blob_2 } from "../components/common/Background/Blob_2";
import { Blob_1 } from "../components/common/Background/Blob_1";

const Home = () => {
  const navigate = useNavigate();
  const PROFILE_IMG =
    "https://i.namu.wiki/i/wXGU6DZbHowc6IB0GYPJpcmdDkLO3TW3MHzjg63jcTJvIzaBKhYqR0l9toBMHTv2OSU4eFKfPOlfrSQpymDJlA.webp";
  const [userName, setUserName] = useState("팝콘");
  const stores = getStores();

  const handleSearchClick = () => {
    navigate("/search");
  };

  const handleStoreDetail = (id: string) => {
    navigate(`/store/detail/${id}`);
  };

  return (
    <Layout>
      <div className="flex min-h-screen flex-col">
        {/* 배경 박스들 */}
        <div className="relative">
          <div className="absolute right-[-130px] top-[-122px] z-10">
            <Blob_1 />
          </div>
          <div className="absolute right-[-140px] top-[-60px]">
            <Blob_2 />
          </div>
        </div>
        {/* 프로필 */}
        <div className="my-12 p-4">
          <div className="flex-shrink-0">
            <img
              src={PROFILE_IMG}
              className="mb-2 h-12 w-12 rounded-full border object-cover"
              alt="Profile image"
            />
          </div>
          <div className="text-3xl font-bold">
            안녕하세요, <br />
            <span className="text-primary">{userName}</span> 님
          </div>
        </div>
        {/* 인기 스토어 */}
        <div className="z-10">
          <h3 className="p-2 text-lg font-semibold">인기 스토어</h3>
          <div className="flex overflow-x-auto whitespace-nowrap pb-4 pl-2 scrollbar-hide">
            {stores.map((store) => (
              <div
                key={store.id}
                className="mr-1 flex-shrink-0 cursor-pointer"
                onClick={() => handleStoreDetail(store.id)}
              >
                <Card
                  variant="mini"
                  title={store.title}
                  imageSrc={store.imageSrc}
                />
              </div>
            ))}
          </div>
        </div>
        {/* 스토어 찾기 */}
        <div>
          <h3 className="mt-2 p-2 text-lg font-semibold">스토어 찾기</h3>
          <div
            className="relative w-full cursor-pointer"
            onClick={handleSearchClick}
          >
            <div className="mb-4 h-12 w-full rounded-full bg-gray-100 py-3 pl-6 pr-10 text-gray-400 shadow-md">
              스토어를 검색해보세요
            </div>
            <div className="absolute inset-y-0 right-4 flex items-center">
              <IoIosSearch className="text-xl text-gray-400" />
            </div>
          </div>
          <div className="flex overflow-x-auto whitespace-nowrap pb-4 pl-2 scrollbar-hide">
            {stores.map((store) => (
              <div
                key={store.id}
                className="mr-1 flex-shrink-0 cursor-pointer"
                onClick={() => handleStoreDetail(store.id)}
              >
                <Card
                  variant="mini"
                  title={store.title}
                  imageSrc={store.imageSrc}
                />
              </div>
            ))}
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default Home;
