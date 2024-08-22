import { StateCreator } from "zustand";

interface StoreState {
  user: { name: string; imageUrl: string }[];
  login: () => Promise<void>;
}

export const createUserSlice: StateCreator<StoreState> = (set) => ({
  user: [{ name: "일태", imageUrl: "" }],
  login: async () => {
    // const result = await API 요청
    const result = [{ name: "", imageUrl: "" }];
    set({ user: result });
  },
});
