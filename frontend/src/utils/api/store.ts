import Axios from "../axios";

// 스토어 검색
export const getStores = async (
  values: string,
  size: number = 10,
  page: number,
  sortType: string,
) => {
  const bodyData = {
    query: values,
    size,
    page,
    sortType,
  };

  const response = await Axios.post("/stores/search", bodyData);
  return response;
};

// 스토어 단일 조회
export const getStoreInfo = async (storeId: string) => {
  const response = await Axios(`/stores/${storeId}`);
  return response;
};

// 내가 가진/방문한 스토어 조회
export const getMyStores = async (isVisit: boolean) => {
  const response = await Axios(`/stores/me`, { params: { isVisit } });
  return response;
};

// 스토어 방문 처리
export const verifyVisit = async (storeId: number, token: string) => {
  const response = await Axios.post(`/stores/${storeId}/visit?token=${token}`);
  return response;
};

// 스토어 등록
export const makeStore = async (formData: FormData) => {
  const response = await Axios.post(`/stores`, formData, {
    headers: {
      "Content-Type": "multipart/form-data",
    },
  });

  return response;
};
