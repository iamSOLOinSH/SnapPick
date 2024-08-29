import { StateCreator } from "zustand";

interface StoreState {
  user: { name: string; imageUrl: string }[];
  visitHistory: {
    name: string;
    location: string;
    price: number;
    visitedAt: string;
  }[];
  payment: {
    name: string;
    quantity: number;
    price: number;
  }[];
  login: () => Promise<void>;
}

export const createUserSlice: StateCreator<StoreState> = (set) => ({
  user: [{ name: "일태", imageUrl: "" }],
  visitHistory: [
    {
      name: "팝업스토어1",
      location: "수원시 영통구",
      price: 57000,
      visitedAt: "2024-08-26T12:00:00Z",
    },
    {
      name: "팝업스토어2",
      location: "강남구 역삼동",
      price: 62000,
      visitedAt: "2024-08-08T12:24:00Z",
    },
    {
      name: "팝업스토어3",
      location: "강서구 방화동",
      price: 120000,
      visitedAt: "2024-02-26T12:00:00Z",
    },
    {
      name: "팝업스토어4",
      location: "수원시 장안구",
      price: 12000,
      visitedAt: "2024-07-12T12:00:00Z",
    },
  ],
  payment: [
    { name: "스토어 상품 1", quantity: 2, price: 32000 },
    { name: "스토어 상품 2", quantity: 3, price: 21000 },
  ],
  login: async () => {
    // const result = await API 요청
    const result = [{ name: "", imageUrl: "" }];
    set({ user: result });
  },
});
