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
  authCode: number,
) => {
  const bodyData = {
    accountNumber,
    authCode,
  };

  const response = await Axios.post("/accounts/identity", bodyData);
  return response;
};
