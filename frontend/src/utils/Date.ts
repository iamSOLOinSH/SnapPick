export const isToday = (dateStr: string) => {
  const date = new Date(dateStr);
  const today = new Date();
  return (
    date.getFullYear() === today.getFullYear() &&
    date.getMonth() === today.getMonth() &&
    date.getDate() === today.getDate()
  );
};

export const formatDate = (dateStr: string, considerToday: boolean) => {
  const date = new Date(dateStr);
  const year = date.getFullYear();
  const month = date.getMonth() + 1;
  const day = date.getDate();
  return isToday(dateStr) && considerToday
    ? "오늘"
    : `${year}년 ${month}월 ${day}일`;
};

export const dayTranslations: { [key: string]: string } = {
  MONDAY: "월",
  TUESDAY: "화",
  WEDNESDAY: "수",
  THURSDAY: "목",
  FRIDAY: "금",
  SATURDAY: "토",
  SUNDAY: "일",
};
