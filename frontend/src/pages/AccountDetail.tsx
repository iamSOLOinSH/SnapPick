import { useState, useEffect } from "react";
import { Layout } from "../components/common/Layout";
import { useNavigate } from "react-router";
import { BackButton } from "../components/common/BackButton";

interface Transaction {
  id: string;
  description: string;
  amount: number;
  date: string;
  type: "deposit" | "withdrawal";
}

const AccountDetail = () => {
  const navigate = useNavigate();

  const [userType, setUserType] = useState("seller");
  const [balance, setBalance] = useState(40000);

  const transactionData: Transaction[] = [
    {
      id: "1",
      description: "SSAFY",
      amount: 1000000,
      date: "2024-09-14 04:44",
      type: "deposit",
    },
    {
      id: "2",
      description: "왕갈비",
      amount: 25000,
      date: "2024-09-11 04:44",
      type: "withdrawal",
    },
    {
      id: "3",
      description: "스타벅스",
      amount: 6000,
      date: "2024-09-17 00:44",
      type: "withdrawal",
    },
    {
      id: "4",
      description: "쿠팡",
      amount: 20000,
      date: "2024-09-17 01:44",
      type: "withdrawal",
    },
    {
      id: "5",
      description: "커피빈",
      amount: 4500,
      date: "2024-09-17 04:44",
      type: "withdrawal",
    },
  ];

  return (
    <Layout>
      <div className="flex min-h-screen flex-col">
        <div className="mb-8 mt-12 flex flex-row items-center py-2">
          <BackButton />
          <h1 className="ml-4 text-2xl font-semibold">계좌조회</h1>
        </div>
        {/* 계좌  */}
        <div className="rounded-lg bg-primary p-6 text-white">
          <h2 className="text-lg font-semibold">계좌 이름</h2>
          <p className="text-sm opacity-80">111-111-1111</p>
          <p className="mt-4 text-3xl font-bold">
            {balance.toLocaleString()}원
          </p>
          <div className="mt-4 flex justify-end">
            <button
              className="mr-2 rounded-md bg-white px-4 py-2 text-primary"
              onClick={() => navigate("/account/transfer")}
            >
              보내기
            </button>
          </div>
        </div>
        {/* 거래 내역 */}
        <div className="mt-2 flex-1 bg-base p-4">
          <h3 className="font-lg mb-4 font-semibold">최근 거래내역</h3>
          {transactionData.map((transaction) => (
            <div
              key={transaction.id}
              className="flex items-center justify-between border-b py-3"
            >
              <div>
                <p className="font-semibold">{transaction.description}</p>
                <p className="text-sm text-gray-500">{transaction.date}</p>
              </div>
              <p
                className={`font-semibold ${transaction.type === "deposit" ? "text-primary" : "text-red"}`}
              >
                {transaction.type === "deposit" ? "+" : "-"}
                {transaction.amount.toLocaleString()}원
              </p>
            </div>
          ))}
        </div>
      </div>
    </Layout>
  );
};

export default AccountDetail;
