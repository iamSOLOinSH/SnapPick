import { Route, Routes } from "react-router-dom";
import Main from "../pages/Main";
import HealthCheck from "../pages/HealthCheck";
import Signup from "../pages/Signup";
import SignupSuccess from "../pages/SignupSuccess";
import PasswordSetup from "../pages/PasswordSetup";
import VisitHistory from "../pages/VisitHistory";
import Receipt from "../pages/Receipt";

const Router = () => {
  return (
    <Routes>
      <Route path="/" element={<Main />} />
      <Route path="/signup" element={<Signup />} />
      <Route path="/signup/success" element={<SignupSuccess />} />
      <Route path="/signup/password" element={<PasswordSetup />} />
      <Route path="/history" element={<VisitHistory />} />
      <Route path="/receipt" element={<Receipt />} />
      <Route path="/health-check" element={<HealthCheck />} />
    </Routes>
  );
};

export default Router;
