import Axios from "../axios";

// 1원 송금 보내기
export const getSendIdentity = async (accountNo: string) => {
  const response = await Axios(`/accounts/identity`, {
    params: { account_no: accountNo },
  });
  return response;
};

// 1원 인증 확인
export const validateIdentity = async (
  accountNumber: string,
  authCode: string,
) => {
  const bodyData = {
    accountNumber,
    authCode,
  };

  const response = await Axios.post("/accounts/identity", bodyData);
  return response;
};

// 주계좌 번호와 금액 확인
export const getAccounts = () => {
  const response = Axios("/accounts");
  return response;
};

// 주계좌 -> 다른 계좌 금액 입금
export const sendAccountTransfer = async (
  accountNumber: string,
  balance: number,
) => {
  const bodyData = {
    accountNumber,
    balance,
  };

  const response = await Axios.post("/accounts/transfer", bodyData);
  return response;
};
