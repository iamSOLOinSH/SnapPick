import { StateCreator } from "zustand";
import { membersInfo } from "../utils/api/memeber";
import { Store, StoreData } from "../types/store";
import { getMyStores } from "../utils/api/store";

interface StoreState {
  user: { name: string; imageUrl: string; role: string };
  storeVisitHistory: Store[];
  storeDataVisitHistory: StoreData[];
  storeList: Store[];
  getUserInfo: () => Promise<void>;
  getStoreVisitHistory: () => Promise<void>;
  getStoreDataVisitHistory: () => Promise<void>;
  getVisitHistory: () => Promise<void>;
}

export const createUserSlice: StateCreator<StoreState> = (set, get) => ({
  user: { name: "", imageUrl: "", role: "" },
  storeVisitHistory: [],
  storeDataVisitHistory: [],
  storeList: [],
  // 판매자
  getStoreVisitHistory: async () => {
    try {
      const response = await getMyStores(false);
      set({ storeVisitHistory: response.data });
    } catch (error) {
      console.error(error);
    }
  },

  // 구매자
  getStoreDataVisitHistory: async () => {
    try {
      const response = await getMyStores(true);
      set({ storeDataVisitHistory: response.data });
    } catch (error) {
      console.error(error);
    }
  },
  getVisitHistory: async () => {
    try {
      await get().getUserInfo();
      const { role } = get().user;

      if (role === "판매자") {
        await get().getStoreVisitHistory();
      } else {
        await get().getStoreDataVisitHistory();
      }
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
