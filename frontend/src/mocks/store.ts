import { Store } from "../types/store";

export const stores: Store[] = [
  {
    id: "1",
    title: "서울 핫플 팝업 스토어",
    imageSrc:
      "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
    imgScrList: [
      "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
      "https://www.nintendo.co.kr/character/kirby/assets/img/about/characters-kirby.png",
      "https://i.namu.wiki/i/wXGU6DZbHowc6IB0GYPJpcmdDkLO3TW3MHzjg63jcTJvIzaBKhYqR0l9toBMHTv2OSU4eFKfPOlfrSQpymDJlA.webp",
      "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
      "https://www.nintendo.co.kr/character/kirby/assets/img/about/characters-kirby.png",
      "https://i.namu.wiki/i/wXGU6DZbHowc6IB0GYPJpcmdDkLO3TW3MHzjg63jcTJvIzaBKhYqR0l9toBMHTv2OSU4eFKfPOlfrSQpymDJlA.webp",
    ],
    location: "서울 강남구",
    description: "운영시간 10:30-20:00",
    startDate: "2024-09-01",
    endDate: "2024-09-30",
  },
  {
    id: "2",
    title: "브랜드 X 콜라보 팝업",
    imageSrc:
      "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
    imgScrList: [
      "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
      "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
      "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
    ],
    location: "서울 홍대입구",
    description: "운영시간 10:30-20:00",
    startDate: "2024-08-15",
    endDate: "2024-09-15",
  },
  {
    id: "3",
    title: "신제품 런칭 팝업 스토어",
    imageSrc:
      "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
    imgScrList: [
      "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
      "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
      "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
    ],
    location: "부산 해운대구",
    description: "운영시간 10:30-20:00",
    startDate: "2024-10-01",
    endDate: "2024-10-31",
  },
  {
    id: "4",
    title: "아티스트 전시 팝업",
    imageSrc:
      "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
    imgScrList: [
      "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
      "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
      "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
    ],
    location: "인천 송도",
    description: "운영시간 10:30-20:00",
    startDate: "2024-09-20",
    endDate: "2024-10-20",
  },
  {
    id: "5",
    title: "계절 한정 팝업 스토어",
    imageSrc:
      "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
    imgScrList: [
      "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
      "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
      "https://shopping-phinf.pstatic.net/main_8451971/84519715566.3.jpg?type=f300",
    ],
    location: "제주 서귀포시",
    description: "운영시간 10:30-20:00",
    startDate: "2024-11-01",
    endDate: "2024-12-31",
  },
];

export const getStores = (): Store[] => {
  return stores;
};
