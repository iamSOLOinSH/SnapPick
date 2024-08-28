import { StateCreator } from "zustand";

interface StoreState {
  store: { name: string; imageUrl: string };
  stores: { name: string; imageUrl: string }[];
  getAllStores: () => Promise<void>;
  getStoreInfo: () => Promise<void>;
}

export const createStoreSlice: StateCreator<StoreState> = (set) => ({
  store: { name: "망곰", imageUrl: "" }, // 조회한 스토어 정보를 담을 변수
  stores: [{ name: "망곰", imageUrl: "" }],
  getAllStores: async () => {
    // const result = await API 요청
    const result = [{ name: "", imageUrl: "" }];
    set({ stores: result });
  },
  getStoreInfo: async () => {
    const result = {
      name: "망곰",
      imageUrl:
        "https://search.pstatic.net/common/?src=http%3A%2F%2Fblogfiles.naver.net%2FMjAyMzExMDZfMTAy%2FMDAxNjk5MjM1MjM5MTMz.7EbD3HKIW0h8oJzFA-JABsotD-jISJOC6xoyNc-nCNsg.FTLQizWqZmTXJkaN2U-PoVKa1IyOSzqRjur0kglfN6Eg.PNG.jhw96jhw%2Fimage.png&type=ff332_332",
    };
    set({ store: result });
  },
});
