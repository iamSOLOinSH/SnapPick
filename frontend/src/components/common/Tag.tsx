import React from "react";
import clsx from "clsx";

const TAG_VARIANTS = {
  default: "px-3 py-2 rounded-full text-sm font-medium",
  primary: "bg-primary text-white",
  green: "bg-green text-white",
  red: "bg-red text-white",
} as const;

type TagProps = {
  className?: string;
  variant?: keyof typeof TAG_VARIANTS;
  content: string;
};

export const Tag: React.FC<TagProps> = ({
  className,
  variant = "primary",
  content,
}) => {
  return (
    <span
      className={clsx(TAG_VARIANTS.default, TAG_VARIANTS[variant], className)}
    >
      {content}
    </span>
  );
};
