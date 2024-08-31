import React from "react";
import { Customer } from "../../types/customer";
import { formatDetailDate } from "../../utils/Date";

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
    <div className="mb-4 flex flex-col justify-between rounded-lg border p-4 shadow-sm">
      <div>
        <h3 className="text-xl font-bold">{customer.customer.name}</h3>
        <p className="text-gray-700">
          전화번호: {customer.customer.phoneNumber}
        </p>
        <p className="text-gray-700">
          구매 금액: ￦{customer.totalPrice.toLocaleString()}
        </p>
        <p className="text-gray-700">
          거래 날짜: {formatDetailDate(customer?.transactedAt)}
        </p>
      </div>
      <div className="mt-2 flex items-end justify-end space-x-2">
        <button
          className="rounded-lg bg-primary px-3 py-1 text-sm text-white hover:bg-primary"
          onClick={() => onViewItems(customer)}
        >
          상품 목록 조회
        </button>
        <button
          className="rounded-lg bg-green px-3 py-1 text-sm text-white hover:bg-green"
          onClick={() => onConfirmReceipt(customer.id)}
        >
          수령 확인
        </button>
      </div>
    </div>
  );
};

export default CustomerCard;
