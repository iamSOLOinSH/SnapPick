import React from "react";
import clsx from "clsx";
import { FaMapMarkerAlt } from "react-icons/fa";

type TicketCardProps = {
  title: string;
  date: string;
  location: string;
  price: number;
  buttonText: string;
  isActive: boolean;
  className?: string;
  onClick?: () => void;
};

export const TicketCard: React.FC<TicketCardProps> = ({
  title,
  date,
  location,
  price,
  buttonText,
  isActive,
  className,
  onClick,
}) => {
  return (
    <div
      className={clsx(
        "relative z-0 flex flex-col space-y-2 rounded-lg border px-6 py-4",
        isActive ? "border-primary text-primary" : "border-darkBase text-black",
        className,
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
          ￦{price.toLocaleString()}
        </div>
        <button
          className={clsx(
            "rounded-lg px-3 py-1 text-sm",
            isActive
              ? "bg-primary text-white hover:bg-secondary hover:text-black"
              : "text-grey-600 bg-gray-300 hover:bg-gray-600 hover:text-white",
          )}
          onClick={onClick}
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
