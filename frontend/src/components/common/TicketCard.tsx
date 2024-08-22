import React from "react";
import clsx from "clsx";
import { FaMapMarkerAlt } from "react-icons/fa";
import { FaBagShopping } from "react-icons/fa6";

type TicketCardProps = {
  title: string;
  date: string;
  location: string;
  seller: string;
  buttonText: string;
  isActive: boolean; // 활성 상태를 나타내는 플래그
};

export const TicketCard: React.FC<TicketCardProps> = ({
  title,
  date,
  location,
  seller,
  buttonText,
  isActive,
}) => {
  return (
    <div
      className={clsx(
        "relative z-0 flex flex-col space-y-2 rounded-lg border px-6 py-4",
        isActive ? "border-primary text-primary" : "border-darkBase text-black",
      )}
    >
      {/* 왼쪽 반원 */}
      <div
        className={clsx(
          "absolute left-[-1px] top-14 z-10 -translate-y-1/2 transform rounded-full",
        )}
      >
        <div
          className={clsx(
            "absolute left-0 top-0 h-8 w-4 rounded-r-full border-b border-r border-t bg-white",
            isActive ? "border-primary" : "border-darkBase",
          )}
        />
      </div>
      {/* 오른쪽 반원 */}
      <div
        className={clsx(
          "absolute right-[-1px] top-1/3 z-10 -translate-y-1/2 transform rounded-full",
        )}
      >
        <div
          className={clsx(
            "absolute right-0 top-0 h-8 w-4 rounded-l-full border-b border-l border-t bg-white",
            isActive ? "border-primary" : "border-darkBase",
          )}
        />
      </div>
      {/* 카드 메인 */}
      <div className="flex justify-between">
        <h3 className={clsx("text-lg font-bold", isActive && "text-primary")}>
          {title}
        </h3>
        <span
          className={clsx("text-sm", isActive ? "text-black" : "text-gray-400")}
        >
          {date}
        </span>
      </div>
      <hr className="my-2 border-dashed" />
      <div className="flex items-center justify-between">
        <div className="flex items-center space-x-2">
          <FaMapMarkerAlt />
          <p className="text-sm">{location}</p>
        </div>
      </div>
      <div className="mt-4 flex justify-between">
        <div className="flex items-center space-x-2">
          <FaBagShopping />
          <p className="font-semibold">{seller}</p>
        </div>
        <button
          className={clsx(
            "rounded-lg px-3 py-1 text-sm",
            isActive
              ? "bg-primary text-white hover:bg-secondary hover:text-black"
              : "bg-gray-300 text-gray-600",
          )}
        >
          {buttonText}
        </button>
      </div>
      <div
        className={clsx(
          "absolute inset-x-0 bottom-0 h-2",
          isActive ? "bg-primary" : "bg-darkBase",
        )}
        style={{
          borderBottomLeftRadius: "0.3rem",
          borderBottomRightRadius: "0.3rem",
        }}
      />
    </div>
  );
};
