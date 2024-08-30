import Axios from "../axios";

// 카트 생성
export const makeCart = () => {
  const response = Axios.post("/cart");
  return response;
};

// 카트 상품 조회
export const getCart = (cartId: number) => {
  const response = Axios(`/cart/${cartId}/items`);
  return response;
};

// 카트 상품 추가
export const addCartItem = (
  cartId: number,
  productId: number,
  quantity: number,
) => {
  const response = Axios.post(`/cart/${cartId}/items`, { productId, quantity });
  return response;
};

// 카트 상품 수정
export const ModifyCartItem = (
  cartId: number,
  itemId: number,
  productId: number,
  quantity: number,
) => {
  const response = Axios.put(`/cart/${cartId}/items/${itemId}`, {
    productId,
    quantity,
  });
  return response;
};

// 카트 상품 삭제
export const deleteCartItem = (cartId: number, itemId: number) => {
  const response = Axios.delete(`/cart/${cartId}/items/${itemId}`);
  return response;
};
