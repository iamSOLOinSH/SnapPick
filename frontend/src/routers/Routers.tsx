import { Route, Routes, Navigate } from "react-router-dom";
import React, { Suspense, lazy } from "react";

const Main = lazy(() => import("../pages/Main"));
const Signup = lazy(() => import("../pages/Signup"));
const SignupSuccess = lazy(() => import("../pages/SignupSuccess"));
const PasswordSetup = lazy(() => import("../pages/PasswordSetup"));
const VisitHistory = lazy(() => import("../pages/VisitHistory"));
const Receipt = lazy(() => import("../pages/Receipt"));
const Home = lazy(() => import("../pages/Home"));
const StoreSearch = lazy(() => import("../pages/StoreSearch"));
const StoreDetail = lazy(() => import("../pages/StoreDetail"));
const ProductCreate = lazy(() => import("../pages/ProductCreate"));
const ProductCreateSuccess = lazy(
  () => import("../pages/ProductCreateSuccess"),
);
const StoreCreate = lazy(() => import("../pages/StoreCreate"));
const StoreCreateSuccess = lazy(() => import("../pages/StoreCreateSuccess"));
const Mypage = lazy(() => import("../pages/Mypage"));
const StockControl = lazy(() => import("../pages/StockControl"));
const StockControlDetail = lazy(() => import("../pages/StockControlDetail"));
const PasswordChange = lazy(() => import("../pages/PasswordChange"));
const Order = lazy(() => import("../pages/Order"));
const Cart = lazy(() => import("../pages/Cart"));
const Products = lazy(() => import("../pages/Products"));
const StoreControl = lazy(() => import("../pages/StoreControl"));
const AccountVerificationStep = lazy(
  () => import("../pages/AccountVerification"),
);
const AccountDetail = lazy(() => import("../pages/AccountDetail"));
const AccountVerificationSuccess = lazy(
  () => import("../components/AccountVerification/AccountVerificationSuccess"),
);
const AccountTransfer = lazy(() => import("../pages/AccountTransfer"));
const AccountList = lazy(() => import("../pages/AccountList"));
const AccountTransferSuccess = lazy(
  () => import("../pages/AccountTransferSuccess"),
);
const ReceiptConfirm = lazy(() => import("../pages/ReceiptConfirm"));

import Spinner from "../components/common/Spinner";

const Router = () => {
  return (
    <Suspense fallback={<Spinner />}>
      <Routes>
        <Route path="/" element={<Main />} />
        <Route path="/home" element={<Home />} />
        <Route path="/profile" element={<Mypage />} />
        <Route path="/stock/:storeId" element={<StockControl />} />
        <Route
          path="/stock/detail/:productId"
          element={<StockControlDetail />}
        />
        <Route path="/store/manage" element={<StoreControl />} />
        <Route path="/search" element={<StoreSearch />} />
        <Route path="/store/create" element={<StoreCreate />} />
        <Route path="/store/create/success" element={<StoreCreateSuccess />} />
        <Route path="/product/create" element={<ProductCreate />} />
        <Route
          path="/product/create/success"
          element={<ProductCreateSuccess />}
        />
        <Route path="/store/detail/:storeId" element={<StoreDetail />} />
        <Route path="/signup" element={<Signup />} />
        <Route path="/signup/success" element={<SignupSuccess />} />
        <Route path="/signup/password" element={<PasswordSetup />} />
        <Route path="/password/change" element={<PasswordChange />} />
        <Route path="/history" element={<VisitHistory />} />
        <Route path="/order" element={<Order />} />
        <Route path="/products" element={<Products />} />
        <Route path="/cart" element={<Cart />} />
        <Route path="/receipt" element={<Receipt />} />
        <Route path="/account/add" element={<AccountVerificationStep />} />
        <Route
          path="/account/add/success"
          element={<AccountVerificationSuccess />}
        />
        <Route path="/account/transfer" element={<AccountTransfer />} />
        <Route
          path="/account/transfer/success"
          element={<AccountTransferSuccess />}
        />
        <Route path="/account/list" element={<AccountList />} />
        <Route path="/account/detail/:accountId" element={<AccountDetail />} />
        <Route path="/store/reception/:storeId" element={<ReceiptConfirm />} />
        <Route path="*" element={<Navigate to="/" replace />} />
      </Routes>
    </Suspense>
  );
};

export default Router;
