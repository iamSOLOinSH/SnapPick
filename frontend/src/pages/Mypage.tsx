import { useEffect } from "react";
import { useNavigate } from "react-router";
import { useBoundStore } from "../store/store";
import { Layout } from "../components/common/Layout";
import { Button } from "../components/common/Button";
import { Blob_1 } from "../components/common/Background/Blob_1";
import { Blob_2 } from "../components/common/Background/Blob_2";
import { FaChevronRight } from "react-icons/fa";

const Mypage = () => {
  const navigate = useNavigate();
  const { user, getUserInfo } = useBoundStore((state) => ({
    user: state.user,
    getUserInfo: state.getUserInfo,
  }));

  const handleHistory = () => {
    if (user.role === "0") {
      navigate("/history");
    } else {
      navigate("/store/manage");
    }
  };

  const handleAccountDetail = () => {
    navigate(`/account/detail/1`);
  };

  useEffect(() => {
    getUserInfo();
  }, [getUserInfo]);

  return (
    <Layout>
      <div className="flex min-h-screen flex-col">
        {/* 배경 박스들 */}
        <div className="relative">
          <div className="absolute left-[-270px] top-[250px] z-10">
            <Blob_1 />
          </div>
          <div className="absolute right-[-250px] top-[110px]">
            <Blob_2 />
          </div>
        </div>
        {/* 프로필 */}
        <div className="mt-2 flex flex-row items-center p-4">
          <div className="flex-shrink-0">
            <img
              src={user.imageUrl || "lay.png"}
              className="mb-2 mr-2 h-10 w-10 rounded-full border object-cover"
              alt="Profile image"
            />
          </div>
          <div className="text-2xl font-bold">
            <span className="text-primary">{user.name}</span> 님
          </div>
        </div>
        <div className="mr-2 text-end">
          <Button
            variant="text"
            content="간편 비밀번호 재설정"
            onClick={() => navigate("/password/change")}
          />
        </div>
        {/* 계좌  */}
        <div
          className="z-20 mb-4 cursor-pointer rounded-lg bg-primary p-4 py-4 text-white shadow-lg"
          onClick={handleAccountDetail}
        >
          <div className="font-semibold">주 계좌</div>
          <div className="mb-8 text-sm">111-1111-1111</div>
          <div className="mt-2 text-right text-2xl font-bold">90,000원</div>
        </div>
        {/* 내 계좌 조회  */}
        <div
          className="z-20 mb-4 flex cursor-pointer flex-row items-center justify-between rounded-lg bg-white p-4 shadow-lg"
          onClick={() => navigate("/account/list")}
        >
          <div className="text-xl font-bold">내 계좌</div>
          <div className="flex flex-row items-center justify-center">
            <div className="text-sm text-gray-700">조회하기</div>
            <FaChevronRight className="text-primary" />
          </div>
        </div>
        {/* 스토어 관리  */}
        <div
          className="z-20 flex cursor-pointer items-center justify-between rounded-lg bg-white p-4 py-6 shadow-lg"
          onClick={handleHistory}
        >
          <div>
            <div className="text-xl font-bold">나의 스토어 관리</div>
            <div className="text-sm text-gray-500">자세히 보기 &gt;</div>
          </div>
          <div className="text-3xl">
            <span role="img" aria-label="pencil">
              ✏️
            </span>
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default Mypage;
