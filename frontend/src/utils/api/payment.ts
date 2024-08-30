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
