import Axios from "../axios";

// 결제 시도
export const payment = (cartId: number) => {
  const response = Axios.post(`/payment?cartId=${cartId}`);
  return response;
};

// 영수증 단일 조회
export const getPayment = (cartId: number) => {
  const response = Axios(`/payment/${cartId}`);
  return response;
};

// 수령 대기 고객 조회
export const getCustomerWaiting = (storeId: string) => {
  const response = Axios(`/payment/status?store_id=${storeId}`);
  return response;
};

// 상품 전달 완료 처리
export const sendDeliveryComplete = (cartId: number) => {
  const response = Axios.put(`/payment/status?cartId=${cartId}`);
  return response;
};
