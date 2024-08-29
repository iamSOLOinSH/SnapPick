import React from "react";
import DaumPostcodeEmbed, { Address } from "react-daum-postcode";

type DaumPostProps = {
  setAddress: (address: string) => void;
  onComplete: () => void;
};

export const DaumPost: React.FC<DaumPostProps> = ({
  setAddress,
  onComplete,
}) => {
  const handleComplete = (data: Address) => {
    let fullAddress = data.address;
    let extraAddress = "";

    if (data.addressType === "R") {
      if (data.bname !== "") {
        extraAddress += data.bname;
      }
      if (data.buildingName !== "") {
        extraAddress +=
          extraAddress !== "" ? `, ${data.buildingName}` : data.buildingName;
      }
      fullAddress += extraAddress !== "" ? ` (${extraAddress})` : "";
    }
    onComplete();
    setAddress(fullAddress);
  };

  return (
    <div className="fixed inset-0 z-50 flex flex-col bg-white">
      <div className="relative flex items-center border-b bg-white p-2">
        <p className="mx-auto">우편번호 검색</p>
        <button onClick={onComplete} className="absolute right-2 px-4 py-2">
          ✕
        </button>
      </div>
      <div className="relative flex-grow">
        <DaumPostcodeEmbed
          autoClose
          onComplete={handleComplete}
          style={{ width: "100%", height: "100%", position: "absolute" }}
        />
      </div>
    </div>
  );
};
