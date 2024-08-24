import { Route, Routes } from "react-router-dom";
import Main from "../pages/Main";
import HealthCheck from "../pages/HealthCheck";
import Signup from "../pages/Signup";

const Router = () => {
  return (
    <Routes>
      <Route path="/" element={<Main />} />
      <Route path="/signup" element={<Signup />} />
      <Route path="/health-check" element={<HealthCheck />} />
    </Routes>
  );
};

export default Router;
