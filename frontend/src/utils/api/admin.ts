import Axios from "../axios";

// 계좌 거래내역 조회
export const getTransactionHistory = async (accountNo: string) => {
  const secret_key = import.meta.env.VITE_MANAGER_SECRET_KEY;
  const response = await Axios(`/manager/history`, {
    params: { account_no: accountNo, secret_key: secret_key },
  });
  return response;
};
