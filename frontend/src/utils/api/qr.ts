import Axios from "../axios";

// QR 코드 검증 및 방문 처리
export const validateQr = (token: string) => {
  const response = Axios(`/stores/qr/validate?token=${token}`);
  return response;
};
