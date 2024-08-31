import { useNavigate, useLocation } from "react-router";

import { Layout } from "../common/Layout";
import { Success } from "../common/Success";
import { Button } from "../common/Button";

import { getAccountList, grantMain } from "../../utils/api/account";

const AccountVerificationSuccess = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const handleMove = async () => {
    if (location.state.fromQr) {
      const accounts = await getAccountList();
      await grantMain(accounts.data[0].accountNumber);
      navigate("/cart", { state: { cartId: location.state.cartId } });
    } else {
      navigate("/profile");
    }
  };

  return (
    <Layout>
      <div className="relative"></div>
      <div className="mt-20 flex min-h-[62vh] flex-col">
        <Success />
        <div className="mt-24 text-center">
          <h2 className="mb-2 text-2xl font-bold text-gray-600">인증 완료</h2>
          <p>계좌 인증이 완료되었습니다.</p>
        </div>
      </div>
      <div className="mx-4 flex flex-col gap-2">
        <Button content="확인" onClick={handleMove} />
      </div>
    </Layout>
  );
};

export default AccountVerificationSuccess;
