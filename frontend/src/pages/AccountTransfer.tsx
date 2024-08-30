import React, { useState } from "react";
import { useParams } from "react-router-dom";
import { Layout } from "../components/common/Layout";
import { Input } from "../components/common/Input";
import { Button } from "../components/common/Button";
import { BackButton } from "../components/common/BackButton";

const AccountTransfer: React.FC = () => {
  const { accountId } = useParams<{ accountId: string }>();
  const [transferAmount, setTransferAmount] = useState("");
  const [recipientAccount, setRecipientAccount] = useState("");

  const handleTransfer = () => {
    // 이체 완료!!
  };

  return (
    <Layout>
      <div className="flex min-h-screen flex-col p-4">
        <div className="mb-8 mt-12 flex flex-row items-center py-2">
          <BackButton />
          <h1 className="ml-4 text-2xl font-semibold">보내기</h1>
        </div>
        {/* 다른 계좌 선택해서 돈 보내기로 변경하기 */}
        <div className="mb-4">
          <Input
            name="recipientAccount"
            placeholder="받는 사람 계좌번호"
            value={recipientAccount}
            onChange={(e) => setRecipientAccount(e.target.value)}
          />
        </div>
        <div className="mb-4">
          <Input
            name="transferAmount"
            placeholder="보낼 금액"
            type="number"
            value={transferAmount}
            onChange={(e) => setTransferAmount(e.target.value)}
          />
        </div>
        <Button content="보내기" onClick={handleTransfer} />
      </div>
    </Layout>
  );
};

export default AccountTransfer;
