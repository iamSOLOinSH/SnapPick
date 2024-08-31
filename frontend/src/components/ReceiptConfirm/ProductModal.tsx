import React from "react";
import { Customer, Item } from "../../types/customer";

interface ProductModalProps {
  customer: Customer;
  onClose: () => void;
}

const ProductModal: React.FC<ProductModalProps> = ({ customer, onClose }) => {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
      <div className="w-full max-w-lg rounded bg-white p-6 shadow-lg">
        <h2 className="mb-4 text-xl font-bold">상품 목록</h2>
        <ul>
          {customer.items.map((item: Item) => (
            <li key={item.id} className="mb-2">
              <p className="text-sm font-bold">{item.product.name}</p>
              <p className="text-sm">수량: {item.quantity}</p>
              <p className="text-sm">
                가격: ￦{item.product.price.toLocaleString()}
              </p>
            </li>
          ))}
        </ul>
        <div className="mt-4 flex justify-end">
          <button
            className="bg-red-500 hover:bg-red-600 rounded px-3 py-1 text-sm text-white"
            onClick={onClose}
          >
            닫기
          </button>
        </div>
      </div>
    </div>
  );
};

export default ProductModal;
