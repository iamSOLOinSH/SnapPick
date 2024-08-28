import React, { useState, useEffect, useCallback } from "react";
import { InputLabel } from "../common/InputLabel";
import { Input } from "../common/Input";
import { Button } from "../common/Button";
import { DaumPost } from "./DaumPost";
import { useBoundStore } from "../../store/store";
import CustomDatePicker from "./CustomDatePicker";

interface StoreCreateStep2Props {
  setIsFormComplete: (isComplete: boolean) => void;
}

interface OperationTimeEntry {
  start: string;
  end: string;
}

interface OperationTime {
  [key: string]: OperationTimeEntry;
}

const daysOfWeek = [
  "MONDAY",
  "TUESDAY",
  "WEDNESDAY",
  "THURSDAY",
  "FRIDAY",
  "SATURDAY",
  "SUNDAY",
];

const dayTranslations: { [key: string]: string } = {
  MONDAY: "월",
  TUESDAY: "화",
  WEDNESDAY: "수",
  THURSDAY: "목",
  FRIDAY: "금",
  SATURDAY: "토",
  SUNDAY: "일",
};

const StoreCreateStep2: React.FC<StoreCreateStep2Props> = ({
  setIsFormComplete,
}) => {
  const {
    baseAddress,
    detailAddress,
    startDate,
    endDate,
    operationTime,
    setBaseAddress,
    setDetailAddress,
    setOperationTime,
    setStartDate,
    setEndDate,
  } = useBoundStore();

  const [showDaumPost, setShowDaumPost] = useState(false);
  const [showAllDays, setShowAllDays] = useState(false);
  const [defaultTime, setDefaultTime] = useState({ start: "", end: "" });

  const handleDaumPost = () => {
    setShowDaumPost(!showDaumPost);
  };

  const handleSetAddress = (address: string) => {
    setBaseAddress(address);
  };

  const handleDateChange = (dates: [Date | null, Date | null]) => {
    const [start, end] = dates;
    if (start && end) {
      setStartDate(start);
      setEndDate(end);
    }
  };

  const handleDefaultTimeChange = (field: "start" | "end", value: string) => {
    setDefaultTime((prev) => {
      const newDefaultTime = { ...prev, [field]: value };
      setOperationTime({
        default: newDefaultTime,
      });

      return newDefaultTime;
    });
  };

  const handleShowAllDays = () => {
    setShowAllDays(true);
    const newOperationTime: { [key: string]: { start: string; end: string } } =
      daysOfWeek.reduce(
        (acc, day) => {
          acc[day] = { ...defaultTime };
          return acc;
        },
        {} as { [key: string]: { start: string; end: string } },
      );
    setOperationTime(newOperationTime);
  };

  const handleTimeChange = (
    day: string,
    field: keyof OperationTimeEntry,
    value: string,
  ) => {
    setOperationTime((prev: OperationTime) => ({
      ...prev,
      [day]: { ...prev[day], [field]: value },
    }));
  };

  const checkFormCompletion = useCallback(() => {
    const isComplete =
      baseAddress !== "" &&
      detailAddress !== "" &&
      startDate !== null &&
      endDate !== null &&
      (showAllDays
        ? Object.keys(operationTime).length === 7
        : defaultTime.start !== "" && defaultTime.end !== "");
    setIsFormComplete(isComplete);
  }, [
    baseAddress,
    detailAddress,
    startDate,
    endDate,
    operationTime,
    defaultTime,
    showAllDays,
    setIsFormComplete,
  ]);

  useEffect(() => {
    checkFormCompletion();
  }, [checkFormCompletion]);

  return (
    <div>
      <div className="mb-4">
        <div className="flex flex-row justify-between">
          <InputLabel name="위치" />
          {baseAddress !== "" && (
            <Button
              variant="text"
              content="주소 변경"
              className="mr-2 mt-2"
              onClick={handleDaumPost}
            />
          )}
        </div>
        <Input
          name="기본 주소"
          placeholder="기본 주소"
          value={baseAddress}
          className="mb-4 cursor-pointer"
          readOnly={true}
          disabled={baseAddress !== ""}
          onClick={handleDaumPost}
        />
        <Input
          name="상세 주소"
          placeholder="상세 주소"
          value={detailAddress}
          onChange={(e) => setDetailAddress(e.target.value)}
        />
      </div>
      <div className="mb-4 w-full">
        <InputLabel name="운영 기간" />
        <CustomDatePicker onChange={handleDateChange} />
      </div>
      <div className="mb-4">
        <InputLabel name="운영 시간" />
        {!showAllDays ? (
          <div className="mb-2 flex flex-row items-center">
            <Input
              name="time-start"
              type="time"
              variant="twoThirds"
              value={defaultTime.start}
              onChange={(e) => handleDefaultTimeChange("start", e.target.value)}
              className="ml-2 cursor-pointer"
            />
            <Input
              name="time-end"
              type="time"
              variant="twoThirds"
              value={defaultTime.end}
              onChange={(e) => handleDefaultTimeChange("end", e.target.value)}
              className="ml-2 cursor-pointer"
            />
          </div>
        ) : (
          daysOfWeek.map((day) => (
            <div key={day} className="mb-4 flex flex-row items-center">
              <InputLabel name={dayTranslations[day]} />
              <Input
                name={`${day}-start`}
                type="time"
                variant="twoThirds"
                value={operationTime[day]?.start || ""}
                onChange={(e) => handleTimeChange(day, "start", e.target.value)}
                className="ml-2 cursor-pointer"
              />
              <Input
                name={`${day}-end`}
                type="time"
                variant="twoThirds"
                value={operationTime[day]?.end || ""}
                onChange={(e) => handleTimeChange(day, "end", e.target.value)}
                className="ml-2 cursor-pointer"
              />
            </div>
          ))
        )}
        {!showAllDays && defaultTime.start && defaultTime.end && (
          <Button
            variant="text"
            content="운영시간이 매일 다른가요?"
            onClick={handleShowAllDays}
            className="ml-4 animate-buttonActivate"
          />
        )}
      </div>

      {showDaumPost && (
        <DaumPost setAddress={handleSetAddress} onComplete={handleDaumPost} />
      )}
    </div>
  );
};

export default StoreCreateStep2;
