import Axios from "../axios";

// 상품 목록 조회
export const getProducts = async (storeId: number) => {
  const response = await Axios(`/products?store_id=${storeId}`);
  return response;
};
