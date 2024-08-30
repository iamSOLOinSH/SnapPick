import { useState } from "react";
import { useNavigate } from "react-router";

import { Layout } from "../components/common/Layout";
import { Blob_1 } from "../components/common/Background/Blob_1";
import { Blob_2 } from "../components/common/Background/Blob_2";
import { Blob_3 } from "../components/common/Background/Blob_3";
import { Button } from "../components/common/Button";
import { Input } from "../components/common/Input";

import debounce from "lodash/debounce";

const Main = () => {
  const navigate = useNavigate();
  const [isAdmin, setIsAdmin] = useState<boolean>(false);

  const handleInputChange = debounce(
    (e: React.ChangeEvent<HTMLInputElement>) => {
      if (e.target.value === import.meta.env.VITE_ADMIN_KEY) {
        navigate("/admin");
      }
    },
    300,
  );

  return (
    <Layout>
      {/* 배경 박스들 */}
      <div className="relative">
        <div className="absolute left-[-46px] top-[-122px] animate-moveBlob1">
          <Blob_2 />
        </div>
        <div className="absolute right-[-122px] top-[-112px] animate-moveBlob2">
          <Blob_1 />
        </div>
        <div className="absolute left-[-267px] top-[329px] animate-moveBlob3">
          <Blob_3 />
        </div>
        <div className="absolute bottom-[-950px] right-[-99px] animate-moveBlob2">
          <Blob_2 />
        </div>
      </div>
      {/* 로고 */}
      <div className="relative z-10 mb-32 mt-64 flex animate-popIn flex-col items-center justify-center">
        <img src="shc_symbol_ci.png" className="h-44 w-44" />
        <h1 className="font-title text-4xl font-bold">SnapPick</h1>
      </div>
      <div className="mr-2 text-end">
        <Button
          variant="text"
          content={!isAdmin ? "관리자로 로그인" : "돌아가기"}
          onClick={() => setIsAdmin(!isAdmin)}
        />
      </div>
      {!isAdmin ? (
        <a
          className="relative z-20 mb-8 flex w-full items-center justify-center rounded-md bg-kakao py-1 hover:ring-1"
          href={import.meta.env.VITE_KAKAO_LOGIN_ADDRESS}
        >
          <svg
            viewBox="0 0 24 24"
            fill="none"
            xmlns="http://www.w3.org/2000/svg"
            className="mb-2 mr-2 h-8 w-8"
          >
            <path
              clipRule="evenodd"
              d="M15 7C10.029 7 6 10.129 6 13.989C6 16.389 7.559 18.505 9.932 19.764L8.933 23.431C8.845 23.754 9.213 24.013 9.497 23.826L13.874 20.921C14.243 20.958 14.618 20.978 15 20.978C19.971 20.978 24 17.849 24 13.989C24 10.129 19.971 7 15 7Z"
              fill="black"
              fillRule="evenodd"
            ></path>
          </svg>
          카카오로 로그인하기
        </a>
      ) : (
        <div className="relative z-20 mb-8">
          <Input
            name="관리자 키"
            placeholder="관리자 키를 입력해주세요."
            onChange={handleInputChange}
            autoComplete="off"
          />
        </div>
      )}
    </Layout>
  );
};

export default Main;
