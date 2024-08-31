import React from "react";
import { Customer } from "../../types/customer";

interface CustomerCardProps {
  customer: Customer;
  onViewItems: (customer: Customer) => void;
  onConfirmReceipt: (customerId: number) => void;
}

const CustomerCard: React.FC<CustomerCardProps> = ({
  customer,
  onViewItems,
  onConfirmReceipt,
}) => {
  return (
    <div className="mb-4 flex flex-col items-center justify-between rounded border p-4 shadow-sm">
      <div>
        <h3 className="text-lg font-bold">{customer.customer.name}</h3>
        <p className="text-sm text-gray-500">
          전화번호: {customer.customer.phoneNumber}
        </p>
        <p className="text-sm text-gray-500">
          구매 금액: ￦{customer.totalPrice.toLocaleString()}
        </p>
        <p className="text-sm text-gray-500">
          거래 날짜: {customer.transactedAt}
        </p>
      </div>
      <div className="mt-2 flex items-center space-x-2">
        <button
          className="rounded bg-primary px-3 py-1 text-sm text-white hover:bg-primary"
          onClick={() => onViewItems(customer)}
        >
          상품 목록 조회
        </button>
        <button
          className="rounded bg-green px-3 py-1 text-sm text-white hover:bg-green"
          onClick={() => onConfirmReceipt(customer.id)}
        >
          수령 확인
        </button>
      </div>
    </div>
  );
};

export default CustomerCard;
