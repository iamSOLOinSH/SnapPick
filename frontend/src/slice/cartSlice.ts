import { StateCreator } from "zustand";

import { getCart } from "../utils/api/cart";

interface CartState {
  cart: {
    id: number;
    quantity: number;
    product: {
      id: number;
      name: string;
      price: number;
      stock: number;
      status: string;
      thumbnailImageUrls: string[];
    };
  }[];
  getCartList: (cartId: number) => Promise<void>;
}

export const createCartSlice: StateCreator<CartState> = (set) => ({
  cart: [],

  getCartList: async (cartId: number) => {
    const result = await getCart(cartId);
    set({ cart: result.data });
  },
});
