import { useState } from "react";
import { Layout } from "../components/common/Layout";
import { Button } from "../components/common/Button";
import { BackButton } from "../components/common/BackButton";
import { Input } from "../components/common/Input";
import { InputLabel } from "../components/common/InputLabel";
import { useNavigate } from "react-router";

// import { getTransactionalInformation } from "../utils/api/admin";

const Admin = () => {
  const navigate = useNavigate();
  const secret_key = import.meta.env.VITE_MANAGER_SECRET_KEY;

  const [secretKey, setSecretKey] = useState("");
  const [accountNumber, setAccountNumber] = useState("");
  // const [transactionalInformation, setTransactionalInformation] = useState<any>(
  //   [],
  // );

  const handleSearch = async () => {
    if (secretKey == secret_key) {
      navigate("/accountTransaction?accountNo=" + accountNumber);
    }
    console.log("secretKey 가 잘못되었습니다");
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
        <div className="mx-4">
          <InputLabel name="secret key" />
          <Input
            type="password"
            name="secret key"
            value={secretKey}
            onChange={(e) => setSecretKey(e.target.value)}
          />
          <div className="h-4" />
          <InputLabel name="계좌 번호" />
          <Input
            name="계좌 번호"
            value={accountNumber}
            onChange={(e) => setAccountNumber(e.target.value)}
          />
        </div>

        <div className="mb-16 mt-72">
          <Button content="조회" onClick={handleSearch} />
        </div>
      </div>
    </Layout>
  );
};

export default Admin;
