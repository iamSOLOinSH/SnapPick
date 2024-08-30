import React, { useState, useEffect } from "react";
import { Input } from "../common/Input";
import { Button } from "../common/Button";

interface AccountVerificationStep2Props {
  onNext: () => void;
  onRestart: () => void;
}

const AccountVerificationStep2: React.FC<AccountVerificationStep2Props> = ({
  onNext,
  onRestart,
}) => {
  const [confirmNumber, setConfirmNumber] = useState("");
  const [isFormComplete, setIsFormComplete] = useState(false);
  const [timeLeft, setTimeLeft] = useState(180); // 3분
  const [verificationError, setVerificationError] = useState<string | null>(
    null,
  );

  useEffect(() => {
    if (timeLeft > 0) {
      const timerId = setTimeout(() => setTimeLeft(timeLeft - 1), 1000);
      return () => clearTimeout(timerId);
    }
  }, [timeLeft]);

  const handleNumberInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.replace(/\D/g, "");
    setConfirmNumber(value);
    setIsFormComplete(value.length === 4);
  };

  const handleVerification = () => {
    if (confirmNumber === "1234") {
      setVerificationError(null);
      onNext(); // 인증 성공
    } else {
      setVerificationError("인증에 실패했습니다. 다시 시도해주세요.");
    }
  };

  const formatTime = (time: number) => {
    const minutes = Math.floor(time / 60);
    const seconds = time % 60;
    return `${minutes.toString().padStart(2, "0")}:${seconds.toString().padStart(2, "0")}`;
  };

  return (
    <div>
      <div className="mb-4">
        <Input
          name="4자리 숫자"
          placeholder="4자리 숫자"
          value={confirmNumber}
          maxLength={4}
          onChange={handleNumberInput}
        />
      </div>
      <div className="mb-4 text-sm text-gray-500">
        남은 시간: {formatTime(timeLeft)}
      </div>
      {verificationError && (
        <div className="mb-4 text-sm text-red">{verificationError}</div>
      )}
      {timeLeft > 0 ? (
        <Button
          content="인증하기"
          onClick={handleVerification}
          disabled={!isFormComplete}
        />
      ) : (
        <Button content="다시 인증하기" onClick={onRestart} />
      )}
    </div>
  );
};

export default AccountVerificationStep2;
