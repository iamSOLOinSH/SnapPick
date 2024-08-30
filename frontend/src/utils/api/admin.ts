import Axios from "../axios";

// 멤버 거래내역 목록 조회
export const getTransactionalInformation = () => {
  const response = Axios("/manager/history");
  return response;
};
