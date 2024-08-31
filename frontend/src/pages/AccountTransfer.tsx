import React, { useState, useEffect } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { Layout } from "../components/common/Layout";
import { Input } from "../components/common/Input";
import { Button } from "../components/common/Button";
import { BackButton } from "../components/common/BackButton";
import { useBoundStore } from "../store/store";
import { sendAccountTransfer } from "../utils/api/account";
import { useSnackbar } from "notistack";
import NoAccount from "../components/AccountVerification/NoAccount";

const AccountTransfer: React.FC = () => {
  const navigate = useNavigate();
  const { enqueueSnackbar } = useSnackbar();
  const { accountId } = useParams<{ accountId: string }>();
  const [selectedAccount, setSelectedAccount] = useState<string | null>(null);
  const [transferAmount, setTransferAmount] = useState("");
  const [recipientAccount, setRecipientAccount] = useState("");
  const [isTransferring, setIsTransferring] = useState(false);
  const [isCheck, setIsCheck] = useState(false);

  const { myAccounts, checkAccountsList } = useBoundStore((state) => ({
    myAccounts: state.myAccounts,
    checkAccountsList: state.checkAccountsList,
  }));

  const handleAccountSelect = (accountNumber: string) => {
    setSelectedAccount(accountNumber);
  };
  useEffect(() => {
    checkAccountsList()
      .then(() => {
        setIsCheck(true);
      })
      .catch((error) => {
        console.error(error);
        setIsCheck(false);
      });
  }, [checkAccountsList]);

  const handleTransfer = async () => {
    if (!selectedAccount || !transferAmount) {
      enqueueSnackbar("계좌와 이체 금액을 선택해주세요.", {
        variant: "default",
      });
      return;
    }

    setIsTransferring(true);
    try {
      const response = await sendAccountTransfer(
        selectedAccount,
        Number(transferAmount),
      );
      if (response.status === 200) {
        navigate("/account/transfer/success");
      } else {
        enqueueSnackbar("이체에 실패했습니다. 다시 시도해주세요.", {
          variant: "error",
        });
      }
    } catch (error) {
      console.error(error);
      enqueueSnackbar("이체 중 오류가 발생했습니다.", {
        variant: "error",
      });
    } finally {
      setIsTransferring(false);
    }
  };

  return (
    <Layout>
      <div className="flex min-h-screen flex-col p-4">
        <div className="mb-4 mt-12 flex flex-row items-center py-2">
          <BackButton />
        </div>
        {isCheck && (
          <div className="mb-4">
            <h1 className="ml-4 text-2xl font-semibold">
              {selectedAccount
                ? "이체 금액을 입력해주세요."
                : "돈을 보낼 계좌를 선택해주세요."}
            </h1>
          </div>
        )}
        {isCheck ? (
          <>
            {!selectedAccount ? (
              <div className="mb-4">
                {myAccounts.map((account, index) => (
                  <div
                    key={account.accountNumber}
                    className="mb-4 animate-fadeInSlideUp cursor-pointer rounded-lg bg-primary p-4 text-white transition-transform duration-300 hover:scale-105"
                    style={{ animationDelay: `${index * 0.12}s` }}
                    onClick={() => handleAccountSelect(account.accountNumber)}
                  >
                    <div className="font-semibold">{account.bankName}</div>
                    <div className="mb-4 text-sm">{account.accountNumber}</div>
                    <div className="text-right text-2xl font-bold">
                      {account.theBalance.toLocaleString()}원
                    </div>
                  </div>
                ))}
              </div>
            ) : (
              <div className="mb-4">
                <Input
                  name="transfermoney"
                  type="number"
                  placeholder="이체 금액을 입력하세요"
                  value={transferAmount}
                  onChange={(e) => setTransferAmount(e.target.value)}
                />
                <Button
                  content="이체하기"
                  onClick={handleTransfer}
                  disabled={isTransferring}
                  className="mt-4 w-full"
                />
              </div>
            )}
          </>
        ) : (
          <NoAccount />
        )}
      </div>
    </Layout>
  );
};

export default AccountTransfer;
