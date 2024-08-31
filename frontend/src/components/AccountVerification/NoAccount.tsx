import React, { FC } from "react";
import { Button } from "../common/Button";
import { useNavigate } from "react-router";
import NO_IMG from "../../assets/images/no_account.png";

const NoAccount: FC = () => {
  const navigate = useNavigate();

  const goToVerification = () => {
    navigate("/account/add", { state: { fromQr: false } });
  };

  return (
    <div className="mt-20 flex flex-col items-center justify-center">
      <img src={NO_IMG} className="mb-4 h-44 w-44" />
      <p>연결된 계좌가 없습니다.</p>
      <p>본인 인증을 통해 계좌를 연결하시겠습니까?</p>
      <Button content="인증하기" onClick={goToVerification} className="mt-4" />
    </div>
  );
};

export default NoAccount;
