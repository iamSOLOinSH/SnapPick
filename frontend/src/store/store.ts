import { create } from "zustand";

import { createStoreSlice } from "../slice/storeSlice";
import { createUserSlice } from "../slice/userSlice";
import { createStoreCreationSlice } from "../slice/storeCreationSlice";
import { createProductSlice } from "../slice/productSlice";
import { createCartSlice } from "../slice/cartSlice";
import { createAccountSlice } from "../slice/accountSlice";

type state = ReturnType<typeof createStoreSlice> &
  ReturnType<typeof createUserSlice> &
  ReturnType<typeof createStoreCreationSlice> &
  ReturnType<typeof createProductSlice> &
  ReturnType<typeof createCartSlice> &
  ReturnType<typeof createAccountSlice>;

export const useBoundStore = create<state>((...a) => ({
  ...createStoreSlice(...a),
  ...createUserSlice(...a),
  ...createStoreCreationSlice(...a),
  ...createProductSlice(...a),
  ...createCartSlice(...a),
  ...createAccountSlice(...a),
}));
