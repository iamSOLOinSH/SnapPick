import Axios from "../axios";

// 결제 시도
export const payment = () => {
  const response = Axios.post("/payment");
  return response;
};
