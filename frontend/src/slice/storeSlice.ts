import { StateCreator } from "zustand";

interface StoreState {
  stores: { name: string; imageUrl: string }[];
  getAllStores: () => Promise<void>;
}

export const createStoreSlice: StateCreator<StoreState> = (set) => ({
  stores: [{ name: "망곰", imageUrl: "" }],
  getAllStores: async () => {
    // const result = await API 요청
    const result = [{ name: "", imageUrl: "" }];
    set({ stores: result });
  },
});
