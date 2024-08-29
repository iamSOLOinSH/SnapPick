import React, { useState, useEffect } from "react";
import { Layout } from "../components/common/Layout";
import { BackButton } from "../components/common/BackButton";
import { useParams } from "react-router";
import { Button } from "../components/common/Button";
import { Card } from "../components/common/Card";
import { PiNotePencilBold } from "react-icons/pi";
import { NumberSelector } from "../components/common/NumberSelector";

const StockControlDetail = () => {
  const { productId } = useParams();

  const [productName, setProductName] = useState("상품명 이름");
  const [quantity, setQuantity] = useState(3);

  const handleIncrease = () => setQuantity(quantity + 1);
  const handleDecrease = () => setQuantity(quantity - 1);

  const handleProductChange = () => {};

  const PRODUCT_IMG =
    "https://www.nintendo.co.kr/character/kirby/assets/img/home/kirby-puffy.png";

  return (
    <Layout>
      <div className="flex min-h-screen flex-col p-4">
        <div className="mb-4 flex items-center justify-between">
          <div className="flex items-center">
            <BackButton />
            <h3 className="ml-4 text-2xl font-bold">재고 관리</h3>
          </div>
        </div>
        <div>
          <h3 className="ml-4 text-xl font-bold">대표사진</h3>
          <div className="mb-4 p-4">
            <img src={PRODUCT_IMG} />
          </div>
          <Card
            variant="status"
            title="상품명"
            content={
              <div className="flex items-center gap-4">
                <span>{productName}</span>
                <PiNotePencilBold />
              </div>
            }
          />
          <Card
            variant="status"
            title="상태"
            content={
              <div className="flex items-center gap-4">
                <div className="h-3 w-3 rounded-full bg-red"></div>
                <span>품절</span>
              </div>
            }
          />
          <Card
            variant="status"
            title="수량"
            content={
              <NumberSelector
                quantity={quantity}
                onIncrease={handleIncrease}
                onDecrease={handleDecrease}
              />
            }
          />
          <div className="mt-4">
            <Button
              variant="primary"
              content="수정하기"
              onClick={handleProductChange}
            />
          </div>
        </div>
      </div>
    </Layout>
  );
};

export default StockControlDetail;
