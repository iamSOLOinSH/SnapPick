import React from "react";
import { useNavigate } from "react-router-dom";
import { Layout } from "../components/common/Layout";
import { BackButton } from "../components/common/BackButton";

interface Account {
  id: number;
  name: string;
  number: string;
  balance: number;
  isPrimary: boolean;
}

const AccounListDetail: React.FC = () => {
  const navigate = useNavigate();
  const accounts: Account[] = [
    {
      id: 1,
      name: "주 계좌",
      number: "111-1111-1111",
      balance: 90000,
      isPrimary: true,
    },
    {
      id: 2,
      name: "부 계좌",
      number: "222-2222-2222",
      balance: 50000,
      isPrimary: false,
    },
  ];

  const handleAccountDetail = (accountId: number) => {
    navigate(`/account/detail/${accountId}`);
  };

  return (
    <Layout>
      <div className="flex min-h-screen flex-col">
        <div className="mb-8 mt-12 flex flex-row items-center py-2">
          <BackButton />
          <h1 className="ml-4 text-2xl font-semibold">계좌 목록 조회</h1>
        </div>
        {accounts.map((account) => (
          <div
            key={account.id}
            className="mb-4 cursor-pointer rounded-lg bg-primary p-4 text-white transition-transform duration-300 hover:scale-105"
            onClick={() => handleAccountDetail(account.id)}
          >
            <div className="font-semibold">
              {account.isPrimary ? "주 계좌" : "계좌"} - {account.name}
            </div>
            <div className="mb-4 text-sm">{account.number}</div>
            <div className="text-right text-2xl font-bold">
              {account.balance.toLocaleString()}원
            </div>
          </div>
        ))}
      </div>
    </Layout>
  );
};

export default AccounListDetail;
