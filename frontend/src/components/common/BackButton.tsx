import { IoChevronBack } from "react-icons/io5";
import clsx from "clsx";

type BackButtonProps = {
  className?: string;
};

export const BackButton = ({ className }: BackButtonProps) => {
  return (
    <button
      className={clsx("rounded border-2 p-2 hover:bg-base", className)}
      onClick={() => window.history.back()}
    >
      <IoChevronBack />
    </button>
  );
};
