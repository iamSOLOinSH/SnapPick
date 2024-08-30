import { useLocation, useNavigate } from "react-router";

import { Layout } from "../components/common/Layout";
import { Success } from "../components/common/Success";
import { Button } from "../components/common/Button";

const ProductCreateSuccess = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const storeId = location?.state;

  return (
    <Layout>
      <div className="relative"></div>
      <div className="mt-20 flex min-h-[62vh] flex-col">
        <Success />
        <div className="mt-24 text-center">
          <h2 className="mb-2 text-2xl font-bold text-gray-600">등록 완료</h2>
          <p>상품 등록이 완료되었습니다.</p>
        </div>
      </div>
      <div className="mx-4 flex flex-col gap-2">
        <Button content="확인" onClick={() => navigate(`/stock/${storeId}`)} />
      </div>
    </Layout>
  );
};

export default ProductCreateSuccess;
