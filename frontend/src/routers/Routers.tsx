import { Route, Routes } from "react-router-dom";
import Main from "../pages/Main";
import HealthCheck from "../pages/HealthCheck";

const Router = () => {
  return (
    <Routes>
      <Route path="/" element={<Main />} />
      <Route path="/health-check" element={<HealthCheck />} />
    </Routes>
  );
};

export default Router;
