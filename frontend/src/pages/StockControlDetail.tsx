import React, { useState, useEffect, useRef } from "react";
import { Layout } from "../components/common/Layout";
import { BackButton } from "../components/common/BackButton";
import { useParams } from "react-router";
import { Button } from "../components/common/Button";
import { Card } from "../components/common/Card";
import { PiNotePencilBold } from "react-icons/pi";
import { NumberSelector } from "../components/common/NumberSelector";
import { Input } from "../components/common/Input";

const StockControlDetail = () => {
  const { productId } = useParams();

  const [productName, setProductName] = useState("상품명 이름");
  const [isEdit, setIsEdit] = useState(false);
  const [editName, setEditName] = useState(productName);
  const [quantity, setQuantity] = useState(3);

  const editRef = useRef<HTMLInputElement>(null);

  const handleIncrease = () => setQuantity(quantity + 1);
  const handleDecrease = () => setQuantity(quantity - 1);

  const handleEdit = () => {
    setIsEdit((prev) => !prev);

    if (!isEdit) {
      setTimeout(() => editRef.current?.focus(), 0);
    } else {
      setProductName(editName);
    }
  };

  const handleKeyDown = (e: React.KeyboardEvent<HTMLInputElement>) => {
    if (e.key === "Enter") {
      handleEdit();
    }
  };

  const handleProductChange = () => {};

  const PRODUCT_IMG =
    "https://www.nintendo.co.kr/character/kirby/assets/img/home/kirby-puffy.png";

  return (
    <Layout>
      <div className="flex min-h-screen flex-col p-4">
        <div className="mb-4 flex items-center justify-between">
          <div className="flex items-center">
            <BackButton />
            <h3 className="ml-4 text-2xl font-bold">재고 상세</h3>
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
                {isEdit ? (
                  <input
                    className="bg-base text-right outline-none"
                    value={editName}
                    onChange={(e) => setEditName(e.target.value)}
                    ref={editRef}
                    maxLength={50}
                    onKeyDown={handleKeyDown}
                  />
                ) : (
                  <span>{productName}</span>
                )}
                <PiNotePencilBold onClick={handleEdit} />
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
