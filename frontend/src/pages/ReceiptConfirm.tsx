import React, { useState, useEffect } from "react";
import { useParams } from "react-router";
import { Layout } from "../components/common/Layout";
import { BackButton } from "../components/common/BackButton";
import { getCustomerWaiting, sendDeliveryComplete } from "../utils/api/payment";
import { Customer } from "../types/customer";
import CustomerCard from "../components/ReceiptConfirm/CustomerCard";
import ProductModal from "../components/ReceiptConfirm/ProductModal";
import { useSnackbar } from "notistack";

const ReceiptConfirm = () => {
  const { storeId } = useParams<{ storeId?: string }>();
  const [customers, setCustomers] = useState<Customer[]>([]);
  const [selectedCustomer, setSelectedCustomer] = useState<Customer | null>(
    null,
  );
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);
  const { enqueueSnackbar } = useSnackbar();

  useEffect(() => {
    const handleCustomerWaiting = async () => {
      try {
        const response = await getCustomerWaiting(storeId || "");
        setCustomers(response.data);
      } catch (error) {
        console.log(error);
      }
    };

    handleCustomerWaiting();
  }, [storeId]);

  const handleViewItems = (customer: Customer) => {
    setSelectedCustomer(customer);
    setIsModalOpen(true);
  };

  const handleConfirmReceipt = async (customerId: number) => {
    try {
      await sendDeliveryComplete(customerId);
      setCustomers(customers.filter((customer) => customer.id !== customerId));
      enqueueSnackbar("수령 확인 되었습니다.", {
        variant: "default",
      });
    } catch (error) {
      console.error(error);
    }
  };

  return (
    <Layout className="bg-white">
      <div className="mb-8 mt-12 flex flex-row items-center py-2">
        <BackButton />
        <h1 className="ml-4 text-2xl font-semibold">수령 확인</h1>
      </div>

      {/* 고객 목록 */}
      <div className="px-4">
        <p className="mb-1 p-1">미수령 건 : {customers?.length} 개</p>

        {customers.map((customer) => (
          <CustomerCard
            key={customer.id}
            customer={customer}
            onViewItems={handleViewItems}
            onConfirmReceipt={handleConfirmReceipt}
          />
        ))}
      </div>

      {/* 상품 목록 모달 */}
      {isModalOpen && selectedCustomer && (
        <ProductModal
          customer={selectedCustomer}
          onClose={() => setIsModalOpen(false)}
        />
      )}
    </Layout>
  );
};

export default ReceiptConfirm;
