import { StateCreator } from "zustand";
import Axios from "../utils/axios";

interface OperationTimeEntry {
  start: string;
  end: string;
}

interface OperationTime {
  [key: string]: OperationTimeEntry;
}

interface StoreCreationState {
  storeName: string;
  selectedPhotos: File[];
  storeDescription: string;
  baseAddress: string;
  detailAddress: string;
  operationTime: OperationTime;
  startDate: Date | null;
  endDate: Date | null;
  tags: string[];
  maxCapacity: string;
}

interface StoreCreationAction extends StoreCreationState {
  setStoreName: (name: string) => void;
  setSelectedPhotos: (photos: File[]) => void;
  setStoreDescription: (description: string) => void;
  setBaseAddress: (address: string) => void;
  setDetailAddress: (address: string) => void;
  setOperationTime: (
    time: OperationTime | ((prev: OperationTime) => OperationTime),
  ) => void;
  setStartDate: (date: Date) => void;
  setEndDate: (date: Date) => void;
  addTag: (tag: string) => void;
  removeTag: (tag: string) => void;
  setMaxCapacity: (capacity: string) => void;

  resetStoreCreation: () => void;
  submitStoreCreation: () => Promise<void>;
}

const initialState: StoreCreationState = {
  storeName: "",
  selectedPhotos: [],
  storeDescription: "",
  baseAddress: "",
  detailAddress: "",
  operationTime: {},
  startDate: null,
  endDate: null,
  tags: [],
  maxCapacity: "",
};

export const createStoreCreationSlice: StateCreator<StoreCreationAction> = (
  set,
  get,
) => ({
  ...initialState,

  setStoreName: (name) => set({ storeName: name }),
  setSelectedPhotos: (photos) => set({ selectedPhotos: photos }),
  setStoreDescription: (description) => set({ storeDescription: description }),
  setBaseAddress: (address) => set({ baseAddress: address }),
  setDetailAddress: (address) => set({ detailAddress: address }),
  setOperationTime: (time) =>
    set((state) => ({
      operationTime:
        typeof time === "function" ? time(state.operationTime) : time,
    })),
  setStartDate: (date) => set({ startDate: date }),
  setEndDate: (date) => set({ endDate: date }),
  addTag: (tag) => set((state) => ({ tags: [...state.tags, tag] })),
  removeTag: (tag) =>
    set((state) => ({ tags: state.tags.filter((t) => t !== tag) })),
  setMaxCapacity: (capacity) => set({ maxCapacity: capacity }),

  resetStoreCreation: () => set(initialState),

  submitStoreCreation: async () => {
    const state = get();

    const formatTime = (time: string) => {
      return time.split(":").slice(0, 2).join(":");
    };

    const createRunningTimes = () => {
      const days = [
        "MONDAY",
        "TUESDAY",
        "WEDNESDAY",
        "THURSDAY",
        "FRIDAY",
        "SATURDAY",
        "SUNDAY",
      ];

      if (
        Object.keys(state.operationTime).length === 1 &&
        state.operationTime["default"]
      ) {
        // 모든 요일의 운영 시간이 동일한 경우
        const { start, end } = state.operationTime["default"];
        return days.map((day) => ({
          dayOfWeek: day,
          startTime: formatTime(start),
          endTime: formatTime(end),
        }));
      } else {
        // 요일별로 개별 지정된 경우
        return days.map((day) => ({
          dayOfWeek: day,
          startTime: formatTime(state.operationTime[day]?.start || "00:00"),
          endTime: formatTime(state.operationTime[day]?.end || "00:00"),
        }));
      }
    };

    const stateValues = {
      name: state.storeName,
      description: state.storeDescription,
      location: state.baseAddress + state.detailAddress,
      latitude: null,
      longitude: null,
      operateStartAt: state.startDate,
      operateEndAt: state.endDate,
      tags: state.tags,
      runningTimes: createRunningTimes(),
    };

    const formData = new FormData();
    formData.append("storeCreateReq", JSON.stringify(stateValues));

    for (let i = 0; i < state.selectedPhotos.length; i++) {
      formData.append("imageFiles", state.selectedPhotos[i]);
    }
    console.log(state.selectedPhotos[0]);

    const response = await Axios.post(`/stores`, formData, {
      headers: {
        "Content-Type": "multipart/form-data",
      },
    });

    console.log(response);

    get().resetStoreCreation();
  },
});
