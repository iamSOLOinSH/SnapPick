import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { useBoundStore } from "../store/store";
import { Layout } from "../components/common/Layout";
import { BackButton } from "../components/common/BackButton";

const AccounListDetail: React.FC = () => {
  const navigate = useNavigate();
  const { myAccounts, checkAccountsList } = useBoundStore((state) => ({
    myAccounts: state.myAccounts,
    checkAccountsList: state.checkAccountsList,
  }));

  useEffect(() => {
    checkAccountsList();
  }, [checkAccountsList]);

  const handleAccountDetail = (index: number) => {
    navigate(`/account/detail/${index}`, {
      state: { account: myAccounts[index] },
    });
  };

  return (
    <Layout>
      <div className="flex min-h-screen flex-col">
        <div className="mb-8 mt-12 flex flex-row items-center py-2">
          <BackButton />
          <h1 className="ml-4 text-2xl font-semibold">계좌 목록 조회</h1>
        </div>
        {myAccounts.map((account, index) => (
          <div
            key={account.accountNumber}
            className="mb-4 cursor-pointer rounded-lg bg-primary p-4 text-white transition-transform duration-300 hover:scale-105"
            onClick={() => handleAccountDetail(index)}
          >
            <div className="font-semibold">{account.bankName}</div>
            <div className="mb-4 text-sm">{account.accountNumber}</div>
            <div className="text-right text-2xl font-bold">
              {account.theBalance.toLocaleString()}원
            </div>
          </div>
        ))}
      </div>
    </Layout>
  );
};

export default AccounListDetail;
