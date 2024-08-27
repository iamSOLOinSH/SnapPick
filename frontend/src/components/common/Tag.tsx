import React from "react";
import clsx from "clsx";

const TAG_VARIANTS = {
  default: "px-3 py-2 rounded-full text-sm font-medium select-none",
  primary: "bg-primary text-white",
  green: "bg-green text-white",
  red: "bg-red text-white",
  gray: "bg-darkBase text-white",
  select: "cursor-pointer hover:opacity-80",
} as const;

type TagProps = {
  className?: string;
  variant?: keyof typeof TAG_VARIANTS;
  content: string;
  value?: string; // 서버 전송 값
  isSelectable?: boolean; // 선택 가능한지
  isSelected?: boolean;
  onClick?: (value: string) => void;
};

export const Tag: React.FC<TagProps> = ({
  className,
  variant = "primary",
  content,
  value,
  isSelectable = false,
  isSelected = false,
  onClick,
}) => {
  const tagClassName = clsx(
    TAG_VARIANTS.default,
    isSelectable && TAG_VARIANTS.select,
    isSelectable
      ? isSelected
        ? TAG_VARIANTS.primary
        : TAG_VARIANTS.gray
      : TAG_VARIANTS[variant],
    className,
  );

  const handleClick = () => {
    if (isSelectable && onClick && value !== undefined && !isSelected) {
      onClick(value);
    }
  };

  return (
    <span className={tagClassName} onClick={handleClick}>
      {content}
    </span>
  );
};
