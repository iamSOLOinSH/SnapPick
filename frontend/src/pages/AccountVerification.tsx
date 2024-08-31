import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import { Layout } from "../components/common/Layout";
import { IoChevronBack } from "react-icons/io5";
import AccountVerificationStep1 from "../components/AccountVerification/AccountVerificationStep1";
import AccountVerificationStep2 from "../components/AccountVerification/AccountVerificationStep2";
import OneWonAnimation from "../components/AccountVerification/OneWonAnimation";
import { getSendIdentity, validateIdentity } from "../utils/api/account";

const AccountVerification = () => {
  const [step, setStep] = useState(1);
  const [showAnimation, setShowAnimation] = useState(false);
  const [accountNo, setAccountNo] = useState("");
  const [error, setError] = useState<string | null>(null);
  const navigate = useNavigate();
  const location = useLocation();

  const handlePrevStep = () => {
    navigate("/profile");
  };

  // 1원 송금 보내기
  const sendOneWon = async (accountNo: string) => {
    try {
      const data = await getSendIdentity(accountNo);
      setShowAnimation(true);
      setError(null);
    } catch (error) {
      console.log(error);
      setError("오류가 발생했습니다. 다시 시도해 주세요.");
    }
  };

  // 1원 인증 확인하기
  const checkVerification = async (confirmNumber: string) => {
    setError("");
    try {
      await validateIdentity(accountNo, confirmNumber);

      navigate("/account/add/success", {
        state: {
          fromQr: location.state.fromQr,
          cartId: location?.state?.cartId,
        },
      });
    } catch (error) {
      console.log(error);
      setError("인증 번호가 올바르지 않습니다. 다시 확인해 주세요.");
    }
  };

  const renderStepComponent = () => {
    if (showAnimation) {
      return (
        <OneWonAnimation
          onNext={() => {
            setShowAnimation(false);
            setStep(2);
          }}
          onCheck={() => {
            window.open(
              `https://snappick.youm.me/accountTransaction?accountNo=${accountNo}`,
              "_blank",
            );
          }}
        />
      );
    }

    switch (step) {
      case 1:
        return (
          <AccountVerificationStep1
            onNext={(value) => {
              sendOneWon(value);
              setAccountNo(value);
            }}
            error={error}
            setError={setError}
          />
        );
      case 2:
        return (
          <AccountVerificationStep2
            onNext={(value) => {
              checkVerification(value);
            }}
            error={error}
            setError={setError}
            onRestart={() => sendOneWon(accountNo)}
          />
        );

      default:
        return null;
    }
  };

  return (
    <Layout>
      <div className="flex min-h-screen flex-col px-2">
        <div className="mt-4">
          <button
            className="rounded border-2 p-2 hover:bg-base"
            onClick={handlePrevStep}
          >
            <IoChevronBack />
          </button>
        </div>
        {!showAnimation && (
          <div className="mt-16">
            <h2 className="mb-4 mt-4 text-2xl font-bold">
              {step === 1 && "인증 번호를 받을 "}
              {step === 2 && "입금 내역을 확인하고"}
              {step === 3 && "스토어 추가 정보를"}
              <br />
              {step === 1 && "계좌 정보를 입력해주세요."}
              {step === 2 && "4자리 숫자를 알려주세요."}
            </h2>
          </div>
        )}
        <div>{renderStepComponent()}</div>
      </div>
    </Layout>
  );
};

export default AccountVerification;
