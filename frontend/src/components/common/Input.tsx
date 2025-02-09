import React, { forwardRef } from "react";
import clsx from "clsx";

const INPUT_VARIANTS = {
  full: "h-14 w-full rounded-full bg-base px-4 text-black shadow-md outline-none focus:ring-2 focus:ring-primary",
  twoThirds:
    "h-14 w-3/4 rounded-full bg-base px-4 text-black shadow-md outline-none focus:ring-2 focus:ring-primary",
  half: "h-14 w-1/3 rounded-full bg-base px-4 text-black shadow-md outline-none focus:ring-2 focus:ring-primary",
  third:
    "h-14 w-1/4 rounded-full bg-base px-4 text-black shadow-md outline-none focus:ring-2 focus:ring-primary",
  quarter:
    "h-14 w-1/5 rounded-md bg-base px-4 text-black shadow-md outline-none focus:ring-2 focus:ring-primary",
  check:
    "h-4 w-4 rounded-full bg-base shadow-md outline-none checked:bg-primary",
  disabled: "bg-gray-300",
} as const;

type InputProps = {
  name: string;
  className?: string;
  variant?: keyof typeof INPUT_VARIANTS;
  type?: string;
  placeholder?: string;
  value?: string;
  checked?: boolean;
  maxLength?: number;
  autoComplete?: string;
  readOnly?: boolean;
  disabled?: boolean;
  onClick?: () => void;
  onChange?: (e: React.ChangeEvent<HTMLInputElement>) => void;
  onKeyDown?: (e: React.KeyboardEvent<HTMLInputElement>) => void;
};

export const Input = forwardRef<HTMLInputElement, InputProps>(
  (
    {
      name,
      className,
      variant = "full",
      type = "text",
      placeholder = "",
      value,
      onChange,
      checked,
      autoComplete = "on",
      maxLength,
      onKeyDown,
      readOnly,
      disabled,
      onClick,
    },
    ref,
  ) => {
    return (
      <input
        name={name}
        className={clsx(
          className,
          INPUT_VARIANTS[variant],
          disabled && INPUT_VARIANTS["disabled"],
        )}
        type={type}
        placeholder={placeholder}
        value={value}
        onChange={onChange}
        onKeyDown={onKeyDown}
        checked={checked}
        maxLength={maxLength}
        autoComplete={autoComplete}
        readOnly={readOnly}
        disabled={disabled}
        onClick={onClick}
        ref={ref}
      />
    );
  },
);

Input.displayName = "Input";
