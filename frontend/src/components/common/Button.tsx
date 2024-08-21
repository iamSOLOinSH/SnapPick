import clsx from "clsx";

const BUTTON_VARIANTS = {
  primary:
    "h-14 w-full rounded-lg bg-primary text-white shadow-md hover:bg-secondary hover:text-black",
  secondary: "h-14 w-full rounded-lg text-black shadow-md hover:bg-base",
} as const;

type ButtonProps = {
  className?: string;
  variant?: keyof typeof BUTTON_VARIANTS;
  content: string;
};

export const Button = ({
  className,
  variant = "primary",
  content,
}: ButtonProps) => {
  return (
    <button className={clsx(className, BUTTON_VARIANTS[variant])}>
      {content}
    </button>
  );
};
