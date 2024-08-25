import React, { useState, useRef, ChangeEvent, KeyboardEvent } from "react";
import { Layout } from "../components/common/Layout";
import { Input } from "../components/common/Input";
import { Button } from "../components/common/Button";
import { Blob_2 } from "../components/common/Background/Blob_2";

type InputRef = HTMLInputElement | null;

const PasswordSetup: React.FC = () => {
  const [password, setPassword] = useState<string[]>(["", "", "", ""]);
  const [isSequential, setIsSequential] = useState<boolean>(false);
  const inputRefs = [
    useRef<InputRef>(null),
    useRef<InputRef>(null),
    useRef<InputRef>(null),
    useRef<InputRef>(null),
  ];

  const handleChange = (index: number, value: string) => {
    if (isNaN(Number(value)) || value.length > 1) return;

    const newPassword = [...password];

    if (index > 0 && value === password[index - 1]) {
      setIsSequential(true);
      newPassword[index] = "";
      setPassword(newPassword);
      return;
    } else {
      setIsSequential(false);
      newPassword[index] = value;
    }

    setPassword(newPassword);

    if (value && index < 3) {
      inputRefs[index + 1].current?.focus();
    }
  };

  const handleKeyDown = (
    index: number,
    event: KeyboardEvent<HTMLInputElement>,
  ) => {
    if (event.key === "Backspace" && !password[index] && index > 0) {
      inputRefs[index - 1].current?.focus();
    }
  };

  const isFormComplete = password.every((char) => char !== "");

  return (
    <Layout>
      <div className="relative">
        <div className="absolute left-[-162px] top-[-212px] animate-moveBlob2">
          <Blob_2 />
        </div>
      </div>
      <div className="mt-48 flex min-h-[48vh] flex-col items-center">
        <h2 className="mb-8 text-2xl font-bold">간편 비밀번호를 입력하세요.</h2>
        <div className="mx-4 flex justify-center gap-4">
          {password.map((char, index) => (
            <Input
              key={index}
              name={`password-${index}`}
              variant="quarter"
              value={char ? "*" : ""}
              onChange={(e: ChangeEvent<HTMLInputElement>) =>
                handleChange(index, e.target.value)
              }
              onKeyDown={(e: KeyboardEvent<HTMLInputElement>) =>
                handleKeyDown(index, e)
              }
              ref={inputRefs[index]}
              maxLength={1}
              className="text-center text-2xl"
            />
          ))}
        </div>
        <div className="mt-4 text-center">
          {isSequential && (
            <p className="animate-shake text-sm text-red">
              연속된 비밀번호는 피해주세요.
            </p>
          )}
        </div>
      </div>
      <div className="mx-4 flex flex-col gap-2" style={{ marginTop: "16px" }}>
        <Button
          content="확인"
          disabled={!isFormComplete}
          onClick={() => alert("비밀번호가 설정되었습니다.")}
        />
        <Button
          variant="secondary"
          content="취소"
          onClick={() => alert("취소되었습니다.")}
        />
      </div>
    </Layout>
  );
};

export default PasswordSetup;
