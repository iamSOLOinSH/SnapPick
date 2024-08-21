import clsx from "clsx";

const INPUT_VARIANTS = {
  full: "h-14 w-full rounded-full bg-base px-4 text-black shadow-md outline-none focus:ring-2 focus:ring-primary",
  twoThirds:
    "h-14 w-3/4 rounded-full bg-base px-4 text-black shadow-md outline-none focus:ring-2 focus:ring-primary",
  half: "h-14 w-1/3 rounded-full bg-base px-4 text-black shadow-md outline-none focus:ring-2 focus:ring-primary",
  third:
    "h-14 w-1/4 rounded-full bg-base px-4 text-black shadow-md outline-none focus:ring-2 focus:ring-primary",
  quarter:
    "h-14 w-1/5 rounded-full bg-base px-4 text-black shadow-md outline-none focus:ring-2 focus:ring-primary",
  check:
    "h-6 w-6 rounded-full bg-base shadow-md outline-none checked:bg-primary",
} as const;

type InputProps = {
  className?: string;
  variant?: keyof typeof INPUT_VARIANTS;
  type?: string;
  placeholder?: string;
};

export const Input = ({
  className,
  variant = "full",
  type = "text",
  placeholder = "",
}: InputProps) => {
  return (
    <input
      className={clsx(className, INPUT_VARIANTS[variant])}
      type={type}
      placeholder={placeholder}
    />
  );
};
