import React from "react";
import { Customer, Item } from "../../types/customer";
import { IoClose } from "react-icons/io5";

interface ProductModalProps {
  customer: Customer;
  onClose: () => void;
}

const ProductModal: React.FC<ProductModalProps> = ({ customer, onClose }) => {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
      <div className="relative w-11/12 max-w-lg rounded-lg bg-white p-6 shadow-lg">
        <button
          className="absolute right-4 top-4 text-gray-500 hover:text-gray-700"
          onClick={onClose}
        >
          <IoClose size={24} />
        </button>

        <h2 className="mb-4 text-xl font-bold text-gray-800">상품 목록</h2>

        <ul className="max-h-64 overflow-y-auto">
          {customer.items.map((item: Item) => (
            <li key={item.id} className="mb-2 border-b pb-2 last:border-none">
              <div className="flex justify-between">
                <p className="font-bold text-gray-700">{item.product.name}</p>
              </div>
              <div className="mt-4 flex flex-row justify-between text-sm text-gray-600">
                <p>가격: ￦{item.product.price.toLocaleString()}</p>
                <p>수량: {item.quantity}</p>
              </div>
            </li>
          ))}
        </ul>
      </div>
    </div>
  );
};

export default ProductModal;
