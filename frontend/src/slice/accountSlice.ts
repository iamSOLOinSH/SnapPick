import { StateCreator } from "zustand";
import { getAccounts, getAccountList } from "../utils/api/account";
import { getPayment } from "../utils/api/payment";

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
  payment: {
    id: number;
    status: string;
    store: {
      id: number;
      name: string;
      location: string;
      images: string[];
    };
    customer: {
      memberId: number;
      name: string;
      phoneNumber: string;
    };
    totalPrice: number;
    transactedAt: string;
    items: {
      id: number;
      product: {
        id: number;
        name: string;
        price: number;
        stock: number;
        status: string;
        thumbnailImageUrls: string[];
      };
      quantity: number;
    }[];
  };
  checkAccounts: () => Promise<void>;
  checkAccountsList: () => Promise<void>;
  checkReceipt: (cartId: number) => Promise<void>;
}

export const createAccountSlice: StateCreator<AccountState> = (set) => ({
  mainAccount: { bankName: "", accountNumber: "", theBalance: 0 },
  payment: {
    id: 0,
    status: "",
    store: { id: 0, name: "", location: "", images: [] },
    customer: { memberId: 0, name: "", phoneNumber: "" },
    totalPrice: 0,
    transactedAt: "",
    items: [],
  },
  myAccounts: [],
  checkAccounts: async () => {
    const result = await getAccounts();
    set({ mainAccount: result.data });
  },
  checkAccountsList: async () => {
    const result = await getAccountList();
    set({ myAccounts: result.data });
  },
  checkReceipt: async (cartId: number) => {
    const result = await getPayment(cartId);
    console.log(result);
    set({ payment: result.data });
  },
});
