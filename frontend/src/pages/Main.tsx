import { Layout } from "../components/common/Layout";

import { Blob_1 } from "../components/common/Background/Blob_1";
import { Blob_2 } from "../components/common/Background/Blob_2";
import { Blob_3 } from "../components/common/Background/Blob_3";

const Main = () => {
  return (
    <Layout>
      {/* 배경 박스들 */}
      <div className="relative">
        <div className="animate-moveBlob1 absolute left-[-56px] top-[-132px]">
          <Blob_2 />
        </div>
        <div className="animate-moveBlob2 absolute right-[-142px] top-[-112px]">
          <Blob_1 />
        </div>
        <div className="animate-moveBlob3 absolute left-[-267px] top-[329px]">
          <Blob_3 />
        </div>
      </div>
      {/* 로고 */}
      <div className="animate-popIn relative z-10 my-56 flex flex-col items-center justify-center">
        <img src="shc_symbol_ci.png" className="h-44 w-44" />
        <h1 className="font-title text-4xl font-bold">SnapPick</h1>
      </div>
      <button className="mb-4 flex w-full items-center justify-center rounded-md bg-kakao py-1 hover:opacity-50">
        <svg
          viewBox="0 0 24 24"
          fill="none"
          xmlns="http://www.w3.org/2000/svg"
          className="mb-2 mr-2 h-8 w-8"
        >
          <path
            clip-rule="evenodd"
            d="M15 7C10.029 7 6 10.129 6 13.989C6 16.389 7.559 18.505 9.932 19.764L8.933 23.431C8.845 23.754 9.213 24.013 9.497 23.826L13.874 20.921C14.243 20.958 14.618 20.978 15 20.978C19.971 20.978 24 17.849 24 13.989C24 10.129 19.971 7 15 7Z"
            fill="black"
            fill-rule="evenodd"
          ></path>
        </svg>
        카카오로 로그인하기
      </button>
    </Layout>
  );
};

export default Main;
