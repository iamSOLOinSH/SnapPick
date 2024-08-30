import { StateCreator } from "zustand";
import { getAccounts } from "../utils/api/account";

interface AccountState {
  mainAccount: {
    bankName: string;
    accountNumber: string;
    theBalance: number;
  };
  checkAccounts: () => Promise<void>;
}

export const createAccountSlice: StateCreator<AccountState> = (set) => ({
  mainAccount: { bankName: "", accountNumber: "", theBalance: 0 },
  checkAccounts: async () => {
    const result = await getAccounts();
    console.log(result);
    set({ mainAccount: result.data });
  },
});
