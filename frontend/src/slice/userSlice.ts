import { StateCreator } from "zustand";
import { membersInfo } from "../utils/api/memeber";
import { Store } from "../types/store";
import { getMyStores } from "../utils/api/store";

interface StoreState {
  user: { name: string; imageUrl: string; role: string };
  visitHistory: Store[];
  storeList: Store[];
  payment: {
    name: string;
    quantity: number;
    price: number;
  }[];
  getUserInfo: () => Promise<void>;
  getVisitHistory: () => Promise<void>;
}

export const createUserSlice: StateCreator<StoreState> = (set, get) => ({
  user: { name: "", imageUrl: "", role: "" },
  visitHistory: [],
  storeList: [],
  payment: [
    { name: "스토어 상품 1", quantity: 2, price: 32000 },
    { name: "스토어 상품 2", quantity: 3, price: 21000 },
  ],
  getVisitHistory: async () => {
    try {
      await get().getUserInfo();
      const { role } = get().user;
      const isVisit = role === "판매자" ? false : true;

      const response = await getMyStores(isVisit);
      const visitHistory = response.data;
      console.log("test");
      set({ visitHistory });
    } catch (error) {
      console.log(error);
    }
  },

  getUserInfo: async () => {
    try {
      const response = await membersInfo();
      const userData = response.data;
      set({ user: userData });
    } catch (error) {
      console.error(error);
    }
  },
});
