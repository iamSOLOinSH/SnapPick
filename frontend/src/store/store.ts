import { create } from "zustand";

import { createStoreSlice } from "../slice/storeSlice";
import { createUserSlice } from "../slice/userSlice";
import { createStoreCreationSlice } from "../slice/storeCreationSlice";

type state = ReturnType<typeof createStoreSlice> &
  ReturnType<typeof createUserSlice> &
  ReturnType<typeof createStoreCreationSlice>;

export const useBoundStore = create<state>((...a) => ({
  ...createStoreSlice(...a),
  ...createUserSlice(...a),
  ...createStoreCreationSlice(...a),
}));
