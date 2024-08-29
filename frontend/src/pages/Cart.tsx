import React, { useState, useEffect, useCallback } from "react";

import { useBoundStore } from "../store/store";
import { useNavigate } from "react-router";

import { formatDate } from "../utils/Date";

import { Layout } from "../components/common/Layout";
import { Card } from "../components/common/Card";
import { NumberSelector } from "../components/common/NumberSelector";
import { Button } from "../components/common/Button";

import { LuMoveRight } from "react-icons/lu";
import { FaRegCreditCard } from "react-icons/fa6";

interface Product {
  title: string;
  imageSrc: string;
  price: number;
  quantity: number;
}

const Cart = () => {
  const navigate = useNavigate();
  const { store, getStoreInfo } = useBoundStore((state) => ({
    store: state.store,
    getStoreInfo: state.getStoreInfo,
  }));

  const [products, setProducts] = useState<Product[]>([
    {
      title: "스토어 상품1",
      imageSrc:
        "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
      price: 15000,
      quantity: 3,
    },
    {
      title: "스토어 상품2",
      imageSrc:
        "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
      price: 20000,
      quantity: 2,
    },
    {
      title: "스토어 상품3",
      imageSrc:
        "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
      price: 25000,
      quantity: 1,
    },
  ]);

  const [account, setAccount] = useState<number>(200000);

  const handleIncrease = (index: number) => {
    const newProducts = [...products];
    newProducts[index].quantity += 1;
    setProducts(newProducts);
  };

  const handleDecrease = (index: number) => {
    const newProducts = [...products];
    if (newProducts[index].quantity > 0) {
      newProducts[index].quantity -= 1;
      setProducts(newProducts);
    }
  };

  const totalAmount = useCallback(() => {
    return products.reduce((acc, cur) => acc + cur.price * cur.quantity, 0);
  }, [products]);

  useEffect(() => {
    getStoreInfo();
  }, [getStoreInfo]);

  return (
    <Layout className="relative px-0 pb-0">
      <div className="relative">
        <div className="absolute right-[-150px] top-[20px] z-0 h-96 w-96 origin-top-right rotate-[15deg] transform bg-secondary" />
        <div className="absolute right-[-240px] top-[160px] z-0 h-[600px] w-[400px] origin-top-right rotate-45 transform bg-primary" />
      </div>
      <h1 className="relative z-20 flex-grow text-center text-xl font-bold">
        구매 요청
      </h1>
      <div className="relative z-10 bg-white bg-opacity-50">
        <div className="my-4 flex flex-col items-center">
          <div className="h-4" />
          <Card variant="mini" imageSrc={store.imageUrl} />
          <h2 className="my-2 text-xl font-bold">{store.name}</h2>
          <p className="mb-4 font-medium text-primary">
            결제 일시 {formatDate(new Date().toISOString(), false)}
          </p>
        </div>
      </div>
      <div className="relative flex h-96 w-full flex-col items-center overflow-y-auto bg-primary px-4 scrollbar-hide">
        {products.map((product, index) => (
          <Card
            key={index}
            variant="product"
            title={product.title}
            imageSrc={product.imageSrc}
            price={product.price}
            totalPrice={product.price * product.quantity}
            toggle={
              <NumberSelector
                quantity={product.quantity}
                onIncrease={() => handleIncrease(index)}
                onDecrease={() => handleDecrease(index)}
              />
            }
          />
        ))}
      </div>
      <div className="relative flex items-center justify-center bg-primary py-4 text-lg text-white">
        <button className="flex items-center">
          더 담기{" "}
          <LuMoveRight className="ml-2 h-8 w-8 rounded-full bg-blue-500 p-1" />
        </button>
      </div>
      <div className="relative flex items-center justify-center bg-primary py-4 pb-4 text-4xl font-semibold text-white">
        총 {totalAmount().toLocaleString()}원
      </div>
      <div className="relative bottom-2 left-0 w-full animate-fadeInSlideUp rounded-tl-xl rounded-tr-xl bg-white">
        <div className="mb-4 ml-8 pt-4">
          <p className="text-xl font-semibold">내 계좌</p>
        </div>
        <Card
          variant="simple"
          title={"잔액 " + account.toLocaleString()}
          subtitle={"결제 후 " + (account - totalAmount()).toLocaleString()}
          icon={<FaRegCreditCard className="h-12 w-12" />}
        />
        <div className="mx-2">
          <Button
            content="결제하기"
            className="mb-4"
            onClick={() => navigate("/receipt")}
          />
        </div>
      </div>
    </Layout>
  );
};

export default Cart;
