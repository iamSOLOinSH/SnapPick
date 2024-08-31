export interface Store {
  id: number;
  name: string;
  description: string;
  location: string;
  operateStartAt: string;
  operateEndAt: string;
  viewCount: number;
  visitCount: number;
  sellerId: null | number;
  tags: string[];
  images: ImageObject[];
  runningTimes: RunningTimesObject[];
  fromQr: boolean;
}

interface ImageObject {
  originImageUrl: string;
  thumbnailImageUrl: string;
}

interface RunningTimesObject {
  dayOfWeek: string;
  startTime: string;
  endTime: string;
}

interface StoreDetailDto {
  storeId: number;
  name: string;
  location: string;
}

interface StoreVisitDto {
  storeVisitId: number;
  cartId: number;
  visitedAt: string;
}

interface CartPurchasedDto {
  cartId: number;
  transactionId: number;
  purchasedAmount: number;
}

interface StoreData {
  storeDetailDto: StoreDetailDto;
  storeVisitDto: StoreVisitDto;
  cartPurchasedDto: CartPurchasedDto;
}

interface Product {
  id: number;
  name: string;
  price: number;
  stock: number;
  status: string;
  thumbnailImageUrls: string[];
}
