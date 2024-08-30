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
