import { FaCheck } from "react-icons/fa6";
import { Card } from "../common/Card";

type runningTimeProps = {
  dayOfWeek: string;
  startTime: string;
  endTime: string;
};

type imagesProps = {
  originImageUrl: string;
  thumbnailImageUrl: string;
};

type payProps = {
  totalPrice: number;
  store: {
    id: number;
    name: string;
    location: string;
    images: imagesProps[];
    runningTimes: runningTimeProps[];
  };
};

export const PaymentSuccess: React.FC<payProps> = ({ totalPrice, store }) => {
  return (
    <div className="animate-appear">
      <section className="flex flex-col items-center justify-center">
        <div
          className="mt-4 flex h-24 w-24 items-center justify-center bg-primary text-white"
          style={{
            clipPath:
              "polygon(50% 0%, 85% 15%, 100% 50%, 85% 85%, 50% 100%, 15% 85%, 0% 50%, 15% 15%)",
          }}
        >
          <FaCheck className="h-12 w-12" />
        </div>
        <h2 className="my-4 text-2xl font-bold">결제 성공</h2>
        <p>{store.name}에</p>
        <p>성공적으로 결제되었습니다.</p>
        <p className="mt-4 text-gray-500">총 금액</p>
        <p className="mb-2 text-2xl font-bold">
          {totalPrice.toLocaleString()}원
        </p>
      </section>
      <hr className="mt-2 w-full border-2 border-dashed" />
      <section>
        <p className="my-4 ml-8 font-semibold text-gray-600">팝업 스토어</p>
        <Card
          variant="simple"
          title={store.name}
          imageSrc={store.images[0].thumbnailImageUrl}
          date="2024년 8월 17일 15:02"
        />
      </section>
    </div>
  );
};
