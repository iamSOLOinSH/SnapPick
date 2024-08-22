import { create } from "zustand";

import { createStoreSlice } from "../slice/storeSlice";
import { createUserSlice } from "../slice/userSlice";

type state = ReturnType<typeof createStoreSlice> &
  ReturnType<typeof createUserSlice>;

export const useBoundStore = create<state>((...a) => ({
  ...createStoreSlice(...a),
  ...createUserSlice(...a),
}));
