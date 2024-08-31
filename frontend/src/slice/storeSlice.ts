import { StateCreator } from "zustand";
import { validateQr } from "../utils/api/qr";
import { getStoreInfo } from "../utils/api/store";

type runningTimeProps = {
  dayOfWeek: string;
  startTime: string;
  endTime: string;
};

type imagesProps = {
  originImageUrl: string;
  thumbnailImageUrl: string;
};

interface StoreState {
  store: {
    id: number;
    name: string;
    location: string;
    images: imagesProps[];
    runningTimes: runningTimeProps[];
  };
  stores: { name: string; imageUrl: string }[];
  getAllStores: () => Promise<void>;
  searchStoreInfo: (storeId: string) => Promise<void>;
  enterStore: (token: string) => Promise<void>;
  clearStore: () => void;
}

export const createStoreSlice: StateCreator<StoreState> = (set) => ({
  store: {
    id: 0,
    name: "",
    location: "",
    images: [],
    runningTimes: [],
  }, // 조회한 스토어 정보를 담을 변수
  stores: [{ name: "망곰", imageUrl: "" }],
  getAllStores: async () => {
    // const result = await API 요청
    const result = [{ name: "", imageUrl: "" }];
    set({ stores: result });
  },
  searchStoreInfo: async (storeId) => {
    const result = await getStoreInfo(storeId);
    set({ store: result.data });
  },
  enterStore: async (token: string) => {
    const result = await validateQr(token);
    console.log(result.data);
    set({ store: result.data });
  },
  clearStore: () => {
    set({
      store: {
        id: 0,
        name: "",
        location: "",
        images: [],
        runningTimes: [],
      },
    });
  },
});
