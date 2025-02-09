import React, { useState } from "react";
import { Input } from "../common/Input";
import { Button } from "../common/Button";

interface AccountVerificationStep1Props {
  onNext: (value: string) => void;
  error: string | null;
  setError: (error: string | null) => void;
}

const AccountVerificationStep1: React.FC<AccountVerificationStep1Props> = ({
  onNext,
  error,
  setError,
}) => {
  const [account, setAccount] = useState("");
  const [isFormComplete, setIsFormComplete] = useState(false);

  const handleNumberInput = (e: React.ChangeEvent<HTMLInputElement>) => {
    const value = e.target.value.replace(/\D/g, "");
    setAccount(value);
    setIsFormComplete(value.length === 16);
  };

  return (
    <div>
      <div className="mb-4">
        <Input
          name="계좌번호"
          placeholder="계좌번호 입력"
          value={account}
          maxLength={16}
          onChange={handleNumberInput}
          autoComplete="off"
        />
      </div>
      {error && <div className="mt-2 text-sm text-red">{error}</div>}
      <div className="mt-9">
        <Button
          content="다음"
          onClick={() => onNext(account)}
          disabled={!isFormComplete}
        />
      </div>
    </div>
  );
};

export default AccountVerificationStep1;
