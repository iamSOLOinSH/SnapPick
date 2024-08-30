import Axios from "../axios";

// 주계좌 번호와 금액 확인
export const getAccounts = () => {
  const response = Axios("/accounts");
  return response;
};
