import React, { forwardRef } from "react";
import clsx from "clsx";

const TEXTAREA_VARIANTS = {
  full: "h-32 w-full rounded-xl bg-base px-6 py-4 text-black shadow-md outline-none focus:ring-2 focus:ring-primary scrollbar-hide resize-none",
} as const;

type TextAreaProps = {
  name: string;
  className?: string;
  variant?: keyof typeof TEXTAREA_VARIANTS;
  placeholder?: string;
  value?: string;
  maxLength?: number;
  autoComplete?: string;
  readOnly?: boolean;
  disabled?: boolean;
  onClick?: () => void;
  onChange?: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
  onKeyDown?: (e: React.KeyboardEvent<HTMLTextAreaElement>) => void;
};

export const TextArea = forwardRef<HTMLTextAreaElement, TextAreaProps>(
  (
    {
      name,
      className,
      variant = "full",
      placeholder = "",
      value,
      autoComplete = "on",
      maxLength,
      onKeyDown,
      readOnly,
      disabled,
      onChange,
      onClick,
    },
    ref,
  ) => {
    return (
      <textarea
        name={name}
        className={clsx(className, TEXTAREA_VARIANTS[variant])}
        placeholder={placeholder}
        value={value}
        onKeyDown={onKeyDown}
        maxLength={maxLength}
        autoComplete={autoComplete}
        readOnly={readOnly}
        disabled={disabled}
        onChange={onChange}
        onClick={onClick}
        ref={ref}
      />
    );
  },
);

TextArea.displayName = "TextArea";
