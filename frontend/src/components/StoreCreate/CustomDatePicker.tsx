import React, { forwardRef, useState } from "react";
import DatePicker from "react-datepicker";
import { ko } from "date-fns/locale";
import "react-datepicker/dist/react-datepicker.css";
import { IoCalendarOutline } from "react-icons/io5";
import { SlArrowLeft, SlArrowRight } from "react-icons/sl";
import { Input } from "../common/Input";

type CustomDatePickerProps = {
  onChange: (dates: [Date | null, Date | null]) => void;
  placeholder?: string;
};

const CustomDatePicker = forwardRef<HTMLInputElement, CustomDatePickerProps>(
  ({ onChange, placeholder = "날짜를 선택하세요" }, ref) => {
    const [dateRange, setDateRange] = useState<[Date | null, Date | null]>([
      null,
      null,
    ]);
    const [startDate, endDate] = dateRange;

    const CustomInput = forwardRef<
      HTMLInputElement,
      { value?: string; onClick?: () => void }
    >(({ value, onClick }, ref) => (
      <div className="relative w-full">
        <Input
          name="datepicker"
          className="w-full cursor-pointer"
          ref={ref}
          value={value}
          onClick={onClick}
          readOnly
          placeholder={placeholder}
        />
        <button
          type="submit"
          className="absolute inset-y-0 right-4 flex cursor-pointer items-center bg-transparent"
        >
          <IoCalendarOutline
            className="text-xl text-gray-400"
            onClick={onClick}
          />
        </button>
      </div>
    ));

    return (
      <div style={{ width: "100%" }}>
        <DatePicker
          selectsRange={true}
          startDate={startDate ?? undefined}
          endDate={endDate ?? undefined}
          onChange={(update: [Date | null, Date | null]) => {
            setDateRange(update);
            onChange(update);
          }}
          locale={ko}
          dateFormat="yyyy-MM-dd"
          customInput={<CustomInput ref={ref} />}
          placeholderText={placeholder}
          className="custom-datepicker w-full"
          renderCustomHeader={({
            monthDate,
            decreaseMonth,
            increaseMonth,
            prevMonthButtonDisabled,
            nextMonthButtonDisabled,
          }) => (
            <div className="flex items-center justify-center text-center">
              <button
                onClick={() => decreaseMonth()}
                disabled={prevMonthButtonDisabled}
                className="mr-2"
              >
                <SlArrowLeft />
              </button>
              <div>
                {monthDate.getFullYear()}년 {monthDate.getMonth() + 1}월
              </div>
              <button
                onClick={() => increaseMonth()}
                disabled={nextMonthButtonDisabled}
                className="ml-2"
              >
                <SlArrowRight />
              </button>
            </div>
          )}
        />
      </div>
    );
  },
);

CustomDatePicker.displayName = "CustomDatePicker";

export default CustomDatePicker;
