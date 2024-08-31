import { useState, useEffect } from "react";
import { grantMain } from "../utils/api/account";
import { Layout } from "../components/common/Layout";
import { useNavigate, useLocation, useParams } from "react-router";
import { BackButton } from "../components/common/BackButton";

const AccountDetail = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const account = location.state.account;

  const handleMain = () => {
    grantMain(account.accountNumber);
    navigate("/home");
  };

  return (
    <Layout>
      <div className="flex min-h-screen flex-col">
        <div className="mb-8 mt-12 flex flex-row items-center py-2">
          <BackButton />
          <h1 className="ml-4 text-2xl font-semibold">계좌조회</h1>
        </div>
        {/* 계좌  */}
        <div className="rounded-lg bg-primary p-6 text-white">
          <h2 className="text-lg font-semibold">{account.bankName}</h2>
          <p className="text-sm opacity-80">{account.accountNumber}</p>
          <p className="mt-4 text-3xl font-bold">
            {account.theBalance.toLocaleString()}원
          </p>
        </div>
        <div className="mx-16 my-4 py-2 text-center">
          <button className="hover:underline" onClick={handleMain}>
            주 계좌로 지정하시겠습니까?
          </button>
        </div>
      </div>
    </Layout>
  );
};

export default AccountDetail;
