import Axios from "../axios";

// 상품 목록 조회
export const getProducts = async (storeId: number) => {
  const response = await Axios(`/products?store_id=${storeId}`);
  return response;
};

// 상품 등록
export const makeProduct = async (storeId: string, formData: FormData) => {
  const response = await Axios.post(`/products?store_id=${storeId}`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });

  return response;
};

// 상품 단건 조회
export const getProduct = async (productId: string) => {
  const response = await Axios(`/products/${productId}`);
  return response;
};

// 상품 수정
export const modifyProduct = async (productId: string, formData: FormData) => {
  const response = await Axios.post(
    `/products?product_id=${productId}`,
    formData,
    {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    },
  );

  return response;
};
