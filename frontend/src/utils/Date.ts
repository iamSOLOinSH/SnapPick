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
