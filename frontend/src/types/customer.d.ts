export interface Customer {
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
  items: Item[];
}

export interface Item {
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
}
