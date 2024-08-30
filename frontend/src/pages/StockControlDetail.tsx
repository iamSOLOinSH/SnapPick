import React, { useState, useEffect, useRef } from "react";
import { Layout } from "../components/common/Layout";
import { BackButton } from "../components/common/BackButton";
import { useParams } from "react-router";
import { Button } from "../components/common/Button";
import { Card } from "../components/common/Card";
import { PiNotePencilBold } from "react-icons/pi";
import { NumberSelector } from "../components/common/NumberSelector";
import { Input } from "../components/common/Input";
import { getProduct, modifyProduct } from "../utils/api/product";

const StockControlDetail = () => {
  const NO_PRODUCT_IMG = "https://s3.youm.me/snappick-product/no_product.png";
  const { productId } = useParams<{ productId: string }>();

  const [productName, setProductName] = useState("");
  const [isEdit, setIsEdit] = useState(false);
  const [editName, setEditName] = useState(productName);
  const [quantity, setQuantity] = useState(3);
  const [productImg, setProductImg] = useState("");
  const [isSoldOut, setIsSoldOut] = useState(false);

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

  const handleGetProduct = async () => {
    try {
      const response = await getProduct(productId || "");
      const productData = response?.data;
      setProductName(productData?.name);
      setEditName(productData?.name);
      setQuantity(productData?.stock);
      setIsSoldOut(productData?.status === "판매가능" ? false : true);

      if (productData?.originImageUrls.length === 0) {
        setProductImg(NO_PRODUCT_IMG);
      } else {
        setProductImg(productData?.originImageUrls[0]);
      }
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    handleGetProduct();
  }, []);

  const handleProductChange = async () => {
    const body = {
      name: productName,
      stock: quantity,
    };

    const formData = new FormData();
    formData.append("productCreateReq", JSON.stringify(body));

    // try {
    //   const response = await modifyProduct(productId, formData);
    // }
  };

  return (
    <Layout>
      <div className="flex min-h-screen flex-col p-4">
        <div className="mb-8 mt-12 flex items-center justify-between">
          <div className="flex items-center">
            <BackButton />
            <h3 className="ml-4 text-2xl font-bold">재고 상세</h3>
          </div>
        </div>
        <div>
          <h3 className="ml-4 text-xl font-bold">대표사진</h3>
          <div className="mb-4 flex justify-center p-4">
            <img src={productImg} />
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
              isSoldOut ? (
                <div className="flex items-center gap-4">
                  <div className="h-3 w-3 rounded-full bg-red"></div>
                  <span>품절</span>
                </div>
              ) : (
                <div className="flex items-center gap-4">
                  <div className="h-3 w-3 rounded-full bg-primary"></div>
                  <span>판매중</span>
                </div>
              )
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
