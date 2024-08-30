import React, { useEffect, useCallback } from "react";

import { payment } from "../utils/api/payment";
import { ModifyCartItem, deleteCartItem } from "../utils/api/cart";

import { useBoundStore } from "../store/store";
import { useNavigate, useLocation } from "react-router";

import { formatDate } from "../utils/Date";

import { Layout } from "../components/common/Layout";
import { Card } from "../components/common/Card";
import { NumberSelector } from "../components/common/NumberSelector";
import { Button } from "../components/common/Button";

import { LuMoveRight } from "react-icons/lu";
import { FaRegCreditCard } from "react-icons/fa6";

const Cart = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const {
    cart,
    getCartList,
    mainAccount,
    checkAccounts,
    store,
    searchStoreInfo,
  } = useBoundStore((state) => ({
    cart: state.cart,
    getCartList: state.getCartList,
    mainAccount: state.mainAccount,
    checkAccounts: state.checkAccounts,
    store: state.store,
    searchStoreInfo: state.searchStoreInfo,
  }));

  const cartId = localStorage.getItem("cartId");

  const handleChange = async (
    itemId: number,
    productId: number,
    quantity: number,
    status: string,
  ) => {
    if (cartId) {
      try {
        if (status === "plus") {
          quantity++;
        } else {
          quantity--;
        }

        await ModifyCartItem(+cartId, itemId, productId, quantity);
        await getCartList(+cartId);
      } catch (error) {
        console.error(error);
      }
    }
  };

  const handleDelete = async (itemId: number) => {
    if (cartId) {
      await deleteCartItem(+cartId, itemId);
      await getCartList(+cartId);
    }
  };

  const totalAmount = useCallback(() => {
    return cart.reduce((acc, cur) => acc + cur.product.price * cur.quantity, 0);
  }, [cart]);

  useEffect(() => {
    if (cartId) getCartList(+cartId);
  }, [getCartList, cartId]);

  useEffect(() => {
    checkAccounts();
  }, [checkAccounts]);

  useEffect(() => {
    if (location.state) {
      searchStoreInfo(location.state.id);
    } else {
      searchStoreInfo(localStorage.getItem("storeId") || "");
    }
  }, [searchStoreInfo, location]);

  const handleMoreCart = () => {
    navigate("/products", { state: { id: location.state.id } });
  };

  const handlePayment = () => {
    if (mainAccount.theBalance - totalAmount() >= 0 && cartId) {
      payment(+cartId)
        .then(() => {
          navigate("/receipt", { state: { id: location.state.id } });
        })
        .catch(() => {
          alert("결제에 실패했습니다.");
        });
    } else {
      alert("잔액이 부족합니다.");
    }
  };

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
          <Card
            variant="mini"
            imageSrc={
              store.images[0]?.thumbnailImageUrl ||
              "https://s3.youm.me/snappick-product/no_product.png"
            }
          />
          <h2 className="my-2 text-xl font-bold">{store.name}</h2>
          <p className="mb-4 font-medium text-primary">
            결제 일시 {formatDate(new Date().toISOString(), false)}
          </p>
        </div>
      </div>
      <div className="relative flex h-96 w-full flex-col items-center overflow-y-auto bg-primary px-4 scrollbar-hide">
        {cart.map((item) => (
          <Card
            key={item.id}
            variant="product"
            title={item.product.name}
            imageSrc={item.product.thumbnailImageUrls[0]}
            price={item.product.price}
            totalPrice={item.product.price * item.quantity}
            toggle={
              <NumberSelector
                quantity={item.quantity}
                onIncrease={() =>
                  handleChange(item.id, item.product.id, item.quantity, "plus")
                }
                onDecrease={() =>
                  handleChange(item.id, item.product.id, item.quantity, "minus")
                }
              />
            }
            onDelete={() => handleDelete(item.id)}
          />
        ))}
      </div>
      <div className="relative flex items-center justify-center bg-primary py-4 text-lg text-white">
        <button className="flex items-center" onClick={handleMoreCart}>
          더 담기
          <LuMoveRight className="ml-2 h-8 w-8 rounded-full bg-blue-500 p-1" />
        </button>
      </div>
      <div className="relative flex items-center justify-center bg-primary py-4 pb-4 text-4xl font-semibold text-white">
        총 {totalAmount().toLocaleString()}원
      </div>
      <div className="relative bottom-2 left-0 w-full animate-fadeInSlideUp rounded-tl-xl rounded-tr-xl bg-white">
        <div className="mb-4 ml-8 pt-4">
          <p className="text-xl font-semibold">
            내 계좌 <span className="text-primary">{mainAccount.bankName}</span>
          </p>
        </div>
        <Card
          variant="simple"
          title={"잔액 " + mainAccount.theBalance?.toLocaleString()}
          subtitle={
            "결제 후 " +
            (mainAccount.theBalance - totalAmount()).toLocaleString()
          }
          icon={<FaRegCreditCard className="h-12 w-12" />}
        />
        <div className="mx-2">
          <Button content="결제하기" className="mb-4" onClick={handlePayment} />
        </div>
      </div>
    </Layout>
  );
};

export default Cart;
