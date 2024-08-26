import { Route, Routes } from "react-router-dom";
import Main from "../pages/Main";
import HealthCheck from "../pages/HealthCheck";
import Signup from "../pages/Signup";
import SignupSuccess from "../pages/SignupSuccess";
import PasswordSetup from "../pages/PasswordSetup";
import Home from "../pages/Home";
import StoreSearch from "../pages/StoreSearch";
import StoreDetail from "../pages/StoreDetail";

const Router = () => {
  return (
    <Routes>
      <Route path="/" element={<Main />} />
      <Route path="/home" element={<Home />} />
      <Route path="/search" element={<StoreSearch />} />
      <Route path="/store/detail/:storeId" element={<StoreDetail />} />
      <Route path="/signup" element={<Signup />} />
      <Route path="/signup/success" element={<SignupSuccess />} />
      <Route path="/signup/password" element={<PasswordSetup />} />
      <Route path="/health-check" element={<HealthCheck />} />
    </Routes>
  );
};

export default Router;
