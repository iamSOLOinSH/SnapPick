import React, {
  useState,
  useEffect,
  useRef,
  ChangeEvent,
  KeyboardEvent,
} from "react";
import { Input } from "../common/Input";
import { Button } from "../common/Button";
import { validateIdentity } from "../../utils/api/account";

type InputRef = HTMLInputElement | null;

interface AccountVerificationStep2Props {
  onNext: () => void;
  onRestart: () => void;
}

const AccountVerificationStep2: React.FC<AccountVerificationStep2Props> = ({
  onNext,
  onRestart,
}) => {
  const [confirmNumber, setConfirmNumber] = useState<string[]>([
    "",
    "",
    "",
    "",
  ]);
  const [timeLeft, setTimeLeft] = useState(180); // 3분
  const [verificationError, setVerificationError] = useState<string | null>(
    null,
  );
  const inputRefs = [
    useRef<InputRef>(null),
    useRef<InputRef>(null),
    useRef<InputRef>(null),
    useRef<InputRef>(null),
  ];

  const handleChange = (index: number, value: string) => {
    if (isNaN(Number(value)) || value.length > 1) return;

    const newConfirmNumber = [...confirmNumber];

    newConfirmNumber[index] = value;

    setConfirmNumber(newConfirmNumber);

    if (value && index < 3) {
      inputRefs[index + 1].current?.focus();
    }
  };

  const handleKeyDown = (
    index: number,
    event: KeyboardEvent<HTMLInputElement>,
  ) => {
    if (event.key === "Backspace" && !confirmNumber[index] && index > 0) {
      inputRefs[index - 1].current?.focus();
    }
  };

  useEffect(() => {
    if (timeLeft > 0) {
      const timerId = setTimeout(() => setTimeLeft(timeLeft - 1), 1000);
      return () => clearTimeout(timerId);
    }
  }, [timeLeft]);

  const handleVerification = async () => {
    try {
      const data = await validateIdentity();
    } catch (error) {
      console.log(error);
    }

    if (confirmNumber === "1234") {
      setVerificationError(null);
      onNext(); // 인증 성공
    } else {
      setVerificationError("인증에 실패했습니다. 다시 시도해주세요.");
    }
  };

  const formatTime = (time: number) => {
    const minutes = Math.floor(time / 60);
    const seconds = time % 60;
    return `${minutes.toString().padStart(2, "0")}:${seconds.toString().padStart(2, "0")}`;
  };

  const isFormComplete = confirmNumber.every((char) => char !== "");

  return (
    <div>
      <div className="mb-4">
        <div className="mx-4 flex justify-center gap-4">
          {confirmNumber.map((char, index) => (
            <Input
              key={index}
              name={`confirmNumber-${index}`}
              variant="quarter"
              value={char}
              onChange={(e: ChangeEvent<HTMLInputElement>) =>
                handleChange(index, e.target.value)
              }
              onKeyDown={(e: KeyboardEvent<HTMLInputElement>) =>
                handleKeyDown(index, e)
              }
              ref={inputRefs[index]}
              maxLength={1}
              className="text-center text-2xl"
              autoComplete="off"
            />
          ))}
        </div>
      </div>
      <div className="mb-4 text-sm text-gray-500">
        남은 시간: {formatTime(timeLeft)}
      </div>
      {verificationError && (
        <div className="mb-4 text-sm text-red">{verificationError}</div>
      )}{" "}
      <div className="mt-8">
        {timeLeft > 0 ? (
          <Button
            content="인증하기"
            onClick={handleVerification}
            disabled={!isFormComplete}
          />
        ) : (
          <Button content="다시 인증하기" onClick={onRestart} />
        )}
      </div>
    </div>
  );
};

export default AccountVerificationStep2;
