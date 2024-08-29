import { useState } from "react";
import { useNavigate } from "react-router";
import { useBoundStore } from "../store/store";
import { useZxing } from "react-zxing";

import { Layout } from "../components/common/Layout";
import { BackButton } from "../components/common/BackButton";
import { Button } from "../components/common/Button";
import { Card } from "../components/common/Card";

const Order: React.FC = () => {
  const navigate = useNavigate();
  const { store } = useBoundStore((state) => ({
    store: state.store,
  }));
  const [result, setResult] = useState("");
  const { ref } = useZxing({
    onDecodeResult(result) {
      setResult(result.getText());
    },
  });

  return (
    <Layout className="relative h-[730px] bg-primary">
      <div className="my-4 flex items-center justify-between">
        <BackButton className="border-1 rounded-xl border-white text-white hover:text-black" />
        <h2 className="flex-grow text-center text-xl font-bold text-white">
          스토어 입장
        </h2>
        <div className="w-8" />
      </div>
      <div className="relative flex flex-col items-center">
        <h3 className="mb-12 text-xl font-semibold text-white">
          QR 코드를 스캔해주세요
        </h3>
        <video ref={ref} className="relative min-h-52 w-2/3 rounded-md" />
        {/* 네 모서리에 위치한 요소들 */}
        <div className="absolute left-10 top-12 h-12 w-12 rounded-tl-md border-l-4 border-t-4 border-white"></div>
        <div className="absolute right-10 top-12 h-12 w-12 rounded-tr-md border-r-4 border-t-4 border-white"></div>
        <div className="absolute -bottom-8 left-10 h-12 w-12 rounded-bl-md border-b-4 border-l-4 border-white"></div>
        <div className="absolute -bottom-8 right-10 h-12 w-12 rounded-br-md border-b-4 border-r-4 border-white"></div>
      </div>
      {result && (
        <div className="absolute bottom-0 left-0 w-full animate-fadeInSlideUp rounded-tl-md rounded-tr-md bg-white">
          <div className="ml-4 pt-4">
            <p className="text-xl font-semibold">이 스토어에 방문하셨나요?</p>
          </div>
          <Card
            variant="store"
            title={store.name}
            subtitle="팝업스토어 소개"
            description={"운영 시간 " + "10:00" + "~" + "20:00"}
            imageSrc="https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzExMDZfMTAy%2FMDAxNjk5MjM1MjM5MTMz.7EbD3HKIW0h8oJzFA-JABsotD-jISJOC6xoyNc-nCNsg.FTLQizWqZmTXJkaN2U-PoVKa1IyOSzqRjur0kglfN6Eg.PNG.jhw96jhw%2Fimage.png&type=ff332_332"
          />
          <div className="mx-2">
            <Button
              content="입장하기"
              className="mb-4"
              onClick={() => navigate("/cart")}
            />
          </div>
        </div>
      )}
    </Layout>
  );
};

export default Order;
