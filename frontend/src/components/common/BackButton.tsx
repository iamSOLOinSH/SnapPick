import { IoChevronBack } from "react-icons/io5";

export const BackButton = () => {
  return (
    <button
      className="rounded border-2 p-2 hover:bg-base"
      onClick={() => window.history.back()}
    >
      <IoChevronBack />
    </button>
  );
};
