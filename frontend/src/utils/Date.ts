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

type runningTimeProps = {
  dayOfWeek: string;
  startTime: string;
  endTime: string;
};

export const checkDayOfWeek = (runningTimes: runningTimeProps[] = []) => {
  const days = [
    "SUNDAY",
    "MONDAY",
    "TUESDAY",
    "WEDNESDAY",
    "THURSDAY",
    "FRIDAY",
    "SATURDAY",
  ];

  const today = new Date();
  const todayDayOfWeek = days[today.getDay()];
  return runningTimes.find((rt) => rt.dayOfWeek === todayDayOfWeek);
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

export const getFormattedDate = () => {
  const now = new Date();

  const year = now.getFullYear();
  const month = now.getMonth() + 1; // getMonth() returns 0-based month
  const date = now.getDate();
  const hours = now.getHours();
  const minutes = now.getMinutes();

  // 월, 일, 시간, 분을 두 자릿수로 포맷팅
  const formattedMonth = month.toString().padStart(1, "0");
  const formattedDate = date.toString().padStart(1, "0");
  const formattedHours = hours.toString().padStart(2, "0");
  const formattedMinutes = minutes.toString().padStart(2, "0");

  return `${year}년 ${formattedMonth}월 ${formattedDate}일 ${formattedHours}:${formattedMinutes}`;
};

console.log(getFormattedDate());
