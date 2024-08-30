import clsx from "clsx";
const BUTTON_VARIANTS = {
  primary:
    "h-14 w-full rounded-lg bg-primary text-white shadow-md hover:bg-secondary hover:ring-1 hover:ring-black hover:text-black disabled:bg-gray-400 disabled:text-gray-200 disabled:cursor-not-allowed disabled:hover:bg-gray-400 disabled:hover:text-gray-200",
  secondary:
    "h-14 w-full rounded-lg text-black shadow-md hover:bg-base disabled:bg-gray-200 disabled:text-gray-400 disabled:cursor-not-allowed disabled:hover:bg-gray-200 disabled:hover:text-gray-400",
  text: "h-8 bg-none text-xs hover:underline ",
} as const;

type ButtonProps = {
  className?: string;
  variant?: keyof typeof BUTTON_VARIANTS;
  content: string;
  disabled?: boolean;
  onClick?: () => void;
};

export const Button = ({
  className,
  variant = "primary",
  content,
  onClick,
  disabled = false,
}: ButtonProps) => {
  return (
    <button
      className={clsx(
        className,
        BUTTON_VARIANTS[variant],
        !disabled && "animate-buttonActivate",
      )}
      onClick={onClick}
      disabled={disabled}
    >
      {content}
    </button>
  );
};
