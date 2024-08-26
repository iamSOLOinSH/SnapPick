import { Layout } from "../components/common/Layout";
import { Card } from "../components/common/Card";
import { Button } from "../components/common/Button";

import { Ribbons } from "../components/common/Background/Ribbons";
import { FaCheck } from "react-icons/fa6";

const PaymentSuccess = () => {
  return (
    <Layout className="bg-primary">
      <header className="relative">
        <div className="absolute left-[32px] top-[-63px] z-0 animate-octagonAppear">
          <Ribbons />
        </div>
        <div className="mt-12 flex justify-center">
          <h1 className="z-10 inline-block rounded-full bg-primary p-2 text-center text-2xl text-white">
            모바일 영수증
          </h1>
        </div>
      </header>
      <main className="relative mt-4 h-64 min-h-[74vh] rounded-lg bg-white">
        <section className="flex flex-col items-center justify-center">
          <div
            className="mt-8 flex h-24 w-24 items-center justify-center bg-primary text-white"
            style={{
              clipPath:
                "polygon(50% 0%, 85% 15%, 100% 50%, 85% 85%, 50% 100%, 15% 85%, 0% 50%, 15% 15%)",
            }}
          >
            <FaCheck className="h-12 w-12" />
          </div>
          <h2 className="my-4 text-2xl font-bold">결제 성공</h2>
          <p>팝업스토어 1에</p>
          <p>성공적으로 결제되었습니다.</p>
          <p className="mt-4 text-gray-500">총 금액</p>
          <p className="text-3xl font-semibold">61,000원</p>
        </section>
        <hr className="mt-2 w-full border-2 border-dashed" />
        <section>
          <p className="my-4 ml-8 font-semibold text-gray-600">팝업 스토어</p>
          <Card
            variant="simple"
            title="팝업 스토어 1"
            imageSrc="https://search.pstatic.net/sunny?src=https%3A%2F%2Fi.namu.wiki%2Fi%2F8XSPz74OmwKAlPxupaSpYLQXgHG86E1drwvqaeNB0LnxJ6Vz73iPKe4C2xlkLNBY18QVXJi4PaZYv8rusG_9bQ.webp&type=fff208_208"
            date="2024년 8월 17일 15:02"
          />
        </section>
        <div className="relative z-20 mx-4">
          <Button content="다음" onClick={() => console.log("ㅋㅋ")} />
        </div>
        {/* 왼쪽 반원 */}
        <div className="absolute left-[-1px] top-[309px] z-10 -translate-y-1/2 transform rounded-full">
          <div className="absolute left-0 top-0 h-8 w-4 rounded-r-full bg-primary" />
        </div>
        {/* 오른쪽 반원 */}
        <div className="absolute right-[-1px] top-[309px] z-10 -translate-y-1/2 transform rounded-full">
          <div className="absolute right-0 top-0 h-8 w-4 rounded-l-full bg-primary" />
        </div>
        {/* 아래 물결 */}
        <svg
          className="absolute bottom-0 left-2 h-40 w-full"
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 1440 320"
          preserveAspectRatio="none"
        >
          <path
            fill="#0046FE"
            fillOpacity="1"
            d="
              M0,320 
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              V320 H0 Z
            "
          ></path>
        </svg>
      </main>
    </Layout>
  );
};

export default PaymentSuccess;
