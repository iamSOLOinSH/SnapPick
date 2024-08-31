import React, { FC } from "react";
import { FaCheckCircle } from "react-icons/fa";
import { Button } from "../common/Button";

interface OneWonAnimationProps {
  onNext: () => void;
  onCheck: () => void;
}

const OneWonAnimation: FC<OneWonAnimationProps> = ({ onNext, onCheck }) => {
  return (
    <div className="mt-20 flex flex-col items-center justify-center">
      <FaCheckCircle className="animate-ping-once mb-4 text-5xl text-green" />
      <div className="mb-2 animate-fadeInSlideUp text-4xl font-bold text-primary">
        1원 전송 완료!
      </div>
      <p className="mb-4 text-gray-600">
        입력하신 계좌로 1원을 입금하였습니다.
      </p>
      <Button content="지금 확인하러 가기" onClick={onCheck} variant="text" />
      <Button content="다음" onClick={onNext} className="mt-4" />
    </div>
  );
};

export default OneWonAnimation;
