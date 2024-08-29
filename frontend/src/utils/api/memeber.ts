import Axios from "../axios";

interface memberDataProps {
  role: number;
  phoneNumber: string;
  businessNumber: string;
  pinCode: string;
}

// 회원가입 정보 입력
export const membersRegister = (memberData: memberDataProps) => {
  const response = Axios.post("/members/register", memberData);
  return response;
};

// 핀코드 재설정
export const membersPincode = (pinCode: string) => {
  const response = Axios.post(`/members/pincode?pin_code=${pinCode}`);
  return response;
};

// 회원정보 확인
export const membersInfo = () => {
  const response = Axios("/members/info");
  return response;
};
