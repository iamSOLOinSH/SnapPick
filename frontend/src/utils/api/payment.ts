import Axios from "../axios";

// 결제 시도
export const payment = (cartId: number) => {
  const response = Axios.post(`/payment?cartId=${cartId}`);
  return response;
};
