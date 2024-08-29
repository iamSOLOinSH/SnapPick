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

import Order from "../pages/Order";
import Cart from "../pages/Cart";
import Products from "../pages/Products";

const Router = () => {
  return (
    <Routes>
      <Route path="/" element={<Main />} />
      <Route path="/home" element={<Home />} />
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
      <Route path="/history" element={<VisitHistory />} />
      <Route path="/order" element={<Order />} />
      <Route path="/products" element={<Products />} />
      <Route path="/cart" element={<Cart />} />
      <Route path="/receipt" element={<Receipt />} />
      <Route path="/health-check" element={<HealthCheck />} />
    </Routes>
  );
};

export default Router;
