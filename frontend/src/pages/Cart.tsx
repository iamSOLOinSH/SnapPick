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

import { useSnackbar } from "notistack";

const Cart = () => {
  const { enqueueSnackbar } = useSnackbar();
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

  const handleMakeAccount = () => {
    navigate("/account/add", { state: { fromQr: true, cartId: cartId } });
  };

  const handlePayment = () => {
    if (mainAccount.theBalance - totalAmount() >= 0 && cartId) {
      payment(+cartId)
        .then(() => {
          navigate("/receipt", { state: { id: location.state.id } });
        })
        .catch(() => {
          enqueueSnackbar("결제에 실패했습니다.", {
            variant: "error",
          });
        });
    } else {
      enqueueSnackbar("잔액이 부족합니다.", {
        variant: "error",
      });
    }
  };

  return (
    <Layout className="relative bg-transparent px-0 pb-20">
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
      <div className="relative bottom-0 left-0 w-full animate-fadeInSlideUp bg-transparent">
        <div className="mb-4 ml-8 pt-4">
          <p className="text-xl font-semibold">
            내 계좌 <span className="text-primary">{mainAccount.bankName}</span>
          </p>
        </div>
        {mainAccount ? (
          <div>
            {" "}
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
              <Button
                content="결제하기"
                className="mb-4"
                onClick={handlePayment}
              />
            </div>
          </div>
        ) : (
          <div className="flex flex-col items-center">
            <div>
              <p className="mb-2">주계좌가 존재하지 않습니다.</p>
              <Button
                variant="secondary"
                content="계좌 등록하러가기"
                onClick={handleMakeAccount}
              />
            </div>
          </div>
        )}
      </div>
    </Layout>
  );
};

export default Cart;
