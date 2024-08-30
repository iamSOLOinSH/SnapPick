import { Card } from "../common/Card";

import { getFormattedDate } from "../../utils/Date";

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
  store: {
    id: number;
    name: string;
    location: string;
    images: imagesProps[];
    runningTimes: runningTimeProps[];
  };
  payment: {
    id: number;
    status: string;
    store: {
      id: number;
      name: string;
      location: string;
      images: string[];
    };
    customer: {
      memberId: number;
      name: string;
      phoneNumber: string;
    };
    totalPrice: number;
    transactedAt: string;
    items: {
      id: number;
      product: {
        id: number;
        name: string;
        price: number;
        stock: number;
        status: string;
        thumbnailImageUrls: string[];
      };
      quantity: number;
    }[];
  };
};

export const PaymentDetail: React.FC<payProps> = ({ payment, store }) => {
  return (
    <div className="animate-appear">
      <section>
        <div className="h-4" />
        <Card
          variant="simple"
          title={store.name}
          imageSrc={store.images[0].thumbnailImageUrl}
          date={getFormattedDate()}
        />
      </section>
      <section className="mt-4 flex flex-col">
        <p className="mb-2 text-center font-semibold text-gray-600">
          구매 상품
        </p>
        <div className="mx-2 h-64 overflow-y-auto scrollbar-hide">
          <table className="min-w-full border-collapse">
            <thead className="bg-white">
              <tr className="text-left text-sm font-medium text-gray-900">
                <th className="sticky top-0 bg-white px-6 py-3">상품명</th>
                <th className="sticky top-0 whitespace-nowrap bg-white px-6 py-3">
                  수량
                </th>
                <th className="sticky top-0 bg-white px-6 py-3">금액</th>
              </tr>
            </thead>
            <tbody>
              {payment.items.map((item, index) => (
                <tr key={index} className="text-sm text-gray-500">
                  <td className="max-w-[150px] px-6 py-4 text-gray-900">
                    {item.product.name}
                  </td>
                  <td className="whitespace-nowrap px-8 py-4">
                    {item.quantity}
                  </td>
                  <td className="whitespace-nowrap px-6 py-4">
                    {item.product.price.toLocaleString() + "원"}
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
        <hr className="mt-2 w-full border-2 border-dashed" />
        <div className="mt-4 text-center">
          <p className="text-gray-600">결제 금액</p>
          <p className="text-2xl font-bold">
            {payment.totalPrice.toLocaleString()}원
          </p>
        </div>
      </section>
    </div>
  );
};
