import axios from "axios";
// import { Cookies } from "react-cookie";

export const BASE_URL = import.meta.env.VITE_BACKEND_ADDRESS;

export const HEADERS = {
  "Access-Control-Allow-Origin": "*",
  "Content-Type": "application/json",
};

export const axiosClient = axios.create({
  baseURL: BASE_URL,
  headers: HEADERS,
});

// const cookies = new Cookies();

const Axios = axios.create({
  baseURL: BASE_URL,
});

Axios.interceptors.request.use(
  (config) => {
    // const token = cookies.get("token");
    // if (token) {
    //   config.headers["Authorization"] = "Bearer " + token;
    //   config.withCredentials = true;
    // }

    const token =
      "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzM4NCJ9.eyJpc3MiOiJpbnl1Yjk4QGdtYWlsLmNvbSIsImlhdCI6MTcyNDgxMDE1MiwiZXhwIjoxNzI1NDE0OTUyLCJzdWIiOiIyMzkwIn0.dm0xkJc5in-EGV_2Wb1zDdZGv0oZPVZDgI1GPmAR3Hery5O0P1A7Lhc-TALA0eNA";
    config.headers["Authorization"] = "Bearer " + token;
    // config.withCredentials = true;
    return config;
  },
  (error) => Promise.reject(error),
);

export default Axios;
