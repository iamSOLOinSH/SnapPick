import { StateCreator } from "zustand";
import { getAccounts, getAccountList } from "../utils/api/account";

interface AccountState {
  mainAccount: {
    bankName: string;
    accountNumber: string;
    theBalance: number;
  };
  myAccounts: {
    bankName: string;
    accountNumber: string;
    theBalance: number;
  }[];
  checkAccounts: () => Promise<void>;
  checkAccountsList: () => Promise<void>;
}

export const createAccountSlice: StateCreator<AccountState> = (set) => ({
  mainAccount: { bankName: "", accountNumber: "", theBalance: 0 },
  myAccounts: [],
  checkAccounts: async () => {
    const result = await getAccounts();
    set({ mainAccount: result.data });
  },
  checkAccountsList: async () => {
    const result = await getAccountList();
    set({ myAccounts: result.data });
  },
});
