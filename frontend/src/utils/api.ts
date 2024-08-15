import axios from "axios";
import { axiosClient } from "./axios";

/* 테스트용 */
export const isConnected = () => {
  return axiosClient.get(`/healthcheck`);
};
