import React from "react";
import SpinnerImg from "../../assets/images/spinner.svg";

export const Spinner: React.FC = () => {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-10">
      <img className="h-16 w-16" src={SpinnerImg} />
    </div>
  );
};

export default Spinner;
