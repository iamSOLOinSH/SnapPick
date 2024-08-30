import React, { useState } from "react";
import { Input } from "../common/Input";
import { Button } from "../common/Button";

interface AccountVerificationStep1Props {
  onNext: () => void;
}

const AccountVerificationStep1: React.FC<AccountVerificationStep1Props> = ({
  onNext,
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
        />
      </div>
      <Button content="다음" onClick={onNext} disabled={!isFormComplete} />
    </div>
  );
};

export default AccountVerificationStep1;
