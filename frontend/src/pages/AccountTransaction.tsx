import React, { useState, useEffect } from "react";
import { getTransactionHistory } from "../utils/api/admin";
import clsx from "clsx";

import { Tag } from "../components/common/Tag";
import { useSearchParams } from "react-router-dom";

interface Transaction {
  balance: string;
  afterBalance: string;
  summary: string;
  transactionType: string;
  time: string;
}

const bankCodeMap = {
  "001": "한국은행",
  "002": "산업은행",
  "003": "기업은행",
  "004": "국민은행",
  "011": "농협은행",
  "020": "우리은행",
  "023": "SC제일은행",
  "027": "시티은행",
  "032": "대구은행",
  "034": "광주은행",
  "035": "제주은행",
  "037": "전북은행",
  "039": "경남은행",
  "045": "새마을금고",
  "081": "KEB하나은행",
  "088": "신한은행",
  "090": "카카오뱅크",
  "999": "싸피은행",
};
const AccountTransaction = () => {
  const [opening, setOpening] = useState<boolean>(true);
  const [data, setData] = useState<Transaction[]>([]);

  const [searchParams] = useSearchParams();
  const accountNo = searchParams.get("accountNo") || "";
  const bankcode = accountNo.slice(0, 3);
  const bankname =
    bankCodeMap[bankcode as keyof typeof bankCodeMap] || "커비은행";
  const bankImgUrl =
    "https://s3.youm.me/snappick-bank/" +
    (bankcode in bankCodeMap ? bankcode : "kirby") +
    ".png";

  const managerHistory = async (accountNo: string): Promise<void> => {
    try {
      const responseData = await getTransactionHistory(accountNo);
      setData(responseData.data);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    if (accountNo && accountNo.length != 16) return;
    managerHistory(accountNo);
  }, [accountNo]);

  return (
    <div className="flex flex-col items-center justify-center bg-white">
      {opening ? (
        <div
          className={clsx(
            "fixed top-0 mx-auto mb-16 flex min-h-screen w-full max-w-md items-center justify-center overflow-hidden border-l-2 border-r-2 p-4",
          )}
        >
          <div className="flex h-full items-center justify-center">
            <div
              className="left-0 top-0 z-50 flex h-full items-center justify-center bg-white"
              onClick={() => {
                setOpening(false);
              }}
            >
              <div className="text-center text-lg font-bold">
                <img className="mx-auto mb-2 h-24" src={bankImgUrl}></img>
                {bankname}
              </div>
            </div>
          </div>
        </div>
      ) : (
        <div
          className={clsx(
            "mx-auto flex min-h-screen w-full max-w-md scroll-smooth border-l-2 border-r-2",
          )}
        >
          <div className="flex w-full flex-col items-center">
            <div className="min-h-full w-full">
              <div className="mt-1 w-screen items-center justify-between py-4 shadow">
                <h2 className="flex-grow text-center text-2xl font-bold">
                  거래 내역
                </h2>
              </div>
              <div className="mb-2 mt-2 w-full">
                <div className="flex flex-col">
                  {/* <div className="text-surface border-red-500 min-w-full border text-left text-sm font-light"> */}

                  {data.map((transaction) => (
                    <div className="border-b border-neutral-200 py-3">
                      <div className="flex flex-row justify-between whitespace-nowrap px-6">
                        <div className="flex font-semibold leading-7">
                          <Tag
                            content={transaction.transactionType}
                            variant={
                              transaction.transactionType.includes("입금")
                                ? "primary"
                                : "red"
                            }
                          />
                          &nbsp;&nbsp;
                          {transaction.transactionType.includes("입금")
                            ? "+"
                            : "-"}
                          {transaction.balance}
                        </div>
                      </div>
                      <div className="whitespace-nowrap px-6 text-right font-bold text-gray-600">
                        {transaction.summary}
                      </div>
                      <div className="flex justify-between whitespace-nowrap px-6 text-sm">
                        {transaction.afterBalance}

                        <div className="text-gray-400">{transaction.time}</div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AccountTransaction;
