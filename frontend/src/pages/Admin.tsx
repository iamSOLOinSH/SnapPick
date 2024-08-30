import { useState } from "react";
import { Layout } from "../components/common/Layout";
import { Button } from "../components/common/Button";
import { BackButton } from "../components/common/BackButton";
import { Input } from "../components/common/Input";
import { InputLabel } from "../components/common/InputLabel";

import { getTransactionalInformation } from "../utils/api/admin";

const Admin = () => {
  const [userKey, setUserKey] = useState("");
  const [accountNumber, setAccountNumber] = useState("");
  const [transactionalInformation, setTransactionalInformation] = useState<any>(
    [],
  );

  const handleSearch = async () => {
    const result = await getTransactionalInformation();
    setTransactionalInformation(result);
    console.log(transactionalInformation);
  };

  return (
    <Layout className="bg-white">
      <div className="min-h-100">
        <div className="mb-8 mt-12 flex items-center justify-between">
          <BackButton />
          <h2 className="flex-grow text-center text-2xl font-bold">
            거래 내역
          </h2>
          <div className="w-8" />
        </div>
        {transactionalInformation.length > 0 ? (
          <div></div>
        ) : (
          <div className="mx-4">
            <InputLabel name="사용자 key" />
            <Input
              name="사용자 key"
              value={userKey}
              onChange={(e) => setUserKey(e.target.value)}
            />
            <div className="h-4" />
            <InputLabel name="계좌 번호" />
            <Input
              name="계좌 번호"
              value={accountNumber}
              onChange={(e) => setAccountNumber(e.target.value)}
            />
          </div>
        )}
        <div className="mb-16 mt-72">
          <Button content="조회" onClick={handleSearch} />
        </div>
      </div>
    </Layout>
  );
};

export default Admin;
