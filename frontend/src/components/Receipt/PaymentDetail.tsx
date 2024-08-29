import { Card } from "../common/Card";

type paymentProps = {
  payment: { name: string; quantity: number; price: number }[];
};

export const PaymentDetail: React.FC<paymentProps> = ({ payment }) => {
  return (
    <div className="animate-appear">
      <section>
        <div className="h-4" />
        <Card
          variant="simple"
          title="팝업 스토어 1"
          imageSrc="https://search.pstatic.net/sunny?src=https%3A%2F%2Fi.namu.wiki%2Fi%2F8XSPz74OmwKAlPxupaSpYLQXgHG86E1drwvqaeNB0LnxJ6Vz73iPKe4C2xlkLNBY18QVXJi4PaZYv8rusG_9bQ.webp&type=fff208_208"
          date="2024년 8월 17일 15:02"
        />
      </section>
      <section className="mt-4 flex flex-col">
        <p className="mb-2 text-center font-semibold text-gray-600">
          구매 상품
        </p>
        <div className="scrollbar-hide mx-2 h-64 overflow-y-auto">
          <table className="min-w-full border-collapse">
            <thead className="bg-white">
              <tr className="text-left text-sm font-medium text-gray-900">
                <th className="sticky top-0 bg-white px-6 py-3">상품명</th>
                <th className="sticky top-0 bg-white px-6 py-3">수량</th>
                <th className="sticky top-0 bg-white px-6 py-3">금액</th>
              </tr>
            </thead>
            <tbody>
              {payment.map((product, index) => (
                <tr key={index} className="text-sm text-gray-500">
                  <td className="whitespace-nowrap px-6 py-4 text-gray-900">
                    {product.name}
                  </td>
                  <td className="whitespace-nowrap px-8 py-4">
                    {product.quantity}
                  </td>
                  <td className="whitespace-nowrap px-6 py-4">
                    {product.price.toLocaleString()}
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
            {payment.reduce((acc, cur) => acc + cur.price, 0).toLocaleString()}
            원
          </p>
        </div>
      </section>
    </div>
  );
};
