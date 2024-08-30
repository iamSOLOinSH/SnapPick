import { StateCreator } from "zustand";
import { getProducts } from "../utils/api/product";

interface ProductState {
  products: {
    id: number;
    name: string;
    price: number;
    stock: number;
    status: string;
    thumbnailImageUrls: string[];
  }[];
  getProducts: (storeId: number) => Promise<void>;
}

export const createProductSlice: StateCreator<ProductState> = (set) => ({
  products: [],

  getProducts: async (storeId: number) => {
    const result = await getProducts(storeId);
    set({ products: result.data });
  },
});
