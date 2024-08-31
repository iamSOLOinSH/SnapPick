import React, { useState, useEffect } from "react";
import { useBoundStore } from "../store/store";

import { useNavigate, useLocation } from "react-router";

import { Layout } from "../components/common/Layout";
import { Button } from "../components/common/Button";
import { Ribbons } from "../components/common/Background/Ribbons";
import { PaymentSuccess } from "../components/Receipt/PaymentSuccess";
import { PaymentDetail } from "../components/Receipt/PaymentDetail";

const Receipt = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { payment, checkReceipt, store, searchStoreInfo } = useBoundStore(
    (state) => ({
      payment: state.payment,
      checkReceipt: state.checkReceipt,
      store: state.store,
      searchStoreInfo: state.searchStoreInfo,
    }),
  );
  const [showDetail, setShowDetail] = useState<boolean>(false);

  const cartId = localStorage.getItem("cartId");

  useEffect(() => {
    if (location.state && cartId) {
      searchStoreInfo(location.state.id);
      checkReceipt(+cartId);
    }
  }, [searchStoreInfo, checkReceipt, location.state, cartId]);

  return (
    <Layout className="bg-primary">
      <header className="relative">
        <div className="absolute left-[32px] top-[-63px] z-0 animate-octagonAppear">
          <Ribbons />
        </div>
        <div className="mt-16 flex justify-center scrollbar-hide">
          <h1 className="z-10 inline-block rounded-full bg-primary p-2 text-center text-2xl text-white">
            모바일 영수증
          </h1>
        </div>
      </header>
      <main className="relative mx-4 mt-4 h-64 min-h-[76vh] rounded-lg bg-white">
        {showDetail ? (
          <PaymentDetail store={store} payment={payment} />
        ) : (
          <PaymentSuccess store={store} payment={payment} />
        )}
        <div className="absolute bottom-4 left-0 right-0 z-30 mx-4">
          {showDetail ? (
            <Button content="확인" onClick={() => navigate("/profile")} />
          ) : (
            <Button content="다음" onClick={() => setShowDetail(true)} />
          )}
        </div>
        {/* 왼쪽 반원 */}
        <div className="absolute left-[-1px] top-[300px] z-10 -translate-y-1/2 transform rounded-full">
          <div className="absolute left-0 top-0 h-8 w-4 rounded-r-full bg-primary" />
        </div>
        {/* 오른쪽 반원 */}
        <div className="absolute right-[-1px] top-[300px] z-10 -translate-y-1/2 transform rounded-full">
          <div className="absolute right-0 top-0 h-8 w-4 rounded-l-full bg-primary" />
        </div>
        {/* 아래 물결 */}
        <svg
          className="absolute bottom-0 left-2 z-20 h-40 w-full"
          xmlns="http://www.w3.org/2000/svg"
          viewBox="0 0 1440 320"
          preserveAspectRatio="none"
        >
          <path
            fill="#0046FE"
            fillOpacity="1"
            d="
              M0,320 
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              c15,20 45,20 60,0
              c15,-20 45,-20 60,0
              V320 H0 Z
            "
          ></path>
        </svg>
      </main>
    </Layout>
  );
};

export default Receipt;
