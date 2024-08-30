import { Route, Routes } from "react-router-dom";
import Main from "../pages/Main";
import HealthCheck from "../pages/HealthCheck";
import Signup from "../pages/Signup";
import SignupSuccess from "../pages/SignupSuccess";
import PasswordSetup from "../pages/PasswordSetup";
import VisitHistory from "../pages/VisitHistory";
import Receipt from "../pages/Receipt";
import Home from "../pages/Home";
import StoreSearch from "../pages/StoreSearch";
import StoreDetail from "../pages/StoreDetail";
import ProductCreate from "../pages/ProductCreate";
import ProductCreateSuccess from "../pages/ProductCreateSuccess";
import StoreCreate from "../pages/StoreCreate";
import StoreCreateSuccess from "../pages/StoreCreateSuccess";
import Mypage from "../pages/Mypage";
import StockControl from "../pages/StockControl";
import StockControlDetail from "../pages/StockControlDetail";
import PasswordChange from "../pages/PasswordChange";
import Order from "../pages/Order";
import Cart from "../pages/Cart";
import Products from "../pages/Products";
import StoreControl from "../pages/StoreControl";
import AccountVerificationStep from "../pages/AccountVerification";
import AccountDetail from "../pages/AccountDetail";
import AccountVerificationSuccess from "../components/AccountVerification/AccountVerificationSuccess";
import AccountTransfer from "../pages/AccountTransfer";
import AccountList from "../pages/AccountList";
import Admin from "../pages/Admin";
import AccountTransaction from "../pages/AccountTransaction";
import AccountTransferSuccess from "../pages/AccountTransferSuccess";

const Router = () => {
  return (
    <Routes>
      <Route path="/" element={<Main />} />
      <Route path="/home" element={<Home />} />
      <Route path="/admin" element={<Admin />} />
      <Route path="/accountTransaction" element={<AccountTransaction />} />
      <Route path="/profile" element={<Mypage />} />
      <Route path="/stock/:storeId" element={<StockControl />} />
      <Route path="/stock/detail/:productId" element={<StockControlDetail />} />
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
      <Route path="/health-check" element={<HealthCheck />} />
    </Routes>
  );
};

export default Router;
