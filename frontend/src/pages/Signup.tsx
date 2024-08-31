import React, { useState, useRef, useEffect } from "react";
import { useNavigate } from "react-router";
import { Cookies } from "react-cookie";

import { Layout } from "../components/common/Layout";
import { InputLabel } from "../components/common/InputLabel";
import { Input } from "../components/common/Input";
import { Button } from "../components/common/Button";
import { Blob_1 } from "../components/common/Background/Blob_1";

const Signup = () => {
  const navigate = useNavigate();
  const cookies = new Cookies();

  const [userType, setUserType] = useState<"buyer" | "seller" | "">("");
  const [businessNumber, setBusinessNumber] = useState("");
  const [contact, setContact] = useState({ part1: "", part2: "", part3: "" });

  const [showContact, setShowContact] = useState(false);
  const [showBusinessNumber, setShowBusinessNumber] = useState(false);

  const part1Ref = useRef<HTMLInputElement>(null);
  const part2Ref = useRef<HTMLInputElement>(null);
  const part3Ref = useRef<HTMLInputElement>(null);

  const params = new URLSearchParams(window.location.search);
  const token = params.get("token");

  useEffect(() => {
    if (token) {
      cookies.set("token", token);
    }
  }, [token]);

  const handleUserTypeChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name } = e.target;
    setUserType(name as "buyer" | "seller");
    setShowContact(true);

    if (
      name === "seller" &&
      contact.part1.length === 3 &&
      contact.part2.length === 4 &&
      contact.part3.length === 4
    ) {
      setShowBusinessNumber(true);
    } else {
      setShowBusinessNumber(false);
    }
  };

  const handleContactChange = (
    e: React.ChangeEvent<HTMLInputElement>,
    part: "part1" | "part2" | "part3",
  ) => {
    const value = e.target.value.replace(/\D/g, "");
    setContact((prevState) => ({
      ...prevState,
      [part]: value,
    }));

    if (part === "part1" && value.length === 3) {
      part2Ref.current?.focus();
    } else if (part === "part2" && value.length === 4) {
      part3Ref.current?.focus();
    }

    if (part === "part3" && value.length === 4 && userType === "seller") {
      setShowBusinessNumber(true);
    }
  };

  const handleBackspace = (
    e: React.KeyboardEvent<HTMLInputElement>,
    part: "part1" | "part2" | "part3",
  ) => {
    if (e.key === "Backspace" && contact[part] === "") {
      if (part === "part2") {
        part1Ref.current?.focus();
      } else if (part === "part3") {
        part2Ref.current?.focus();
      }
    }
  };

  const handleNext = () => {
    navigate("/signup/password", {
      state: {
        role: +!!(userType === "seller"),
        phoneNumber: `${contact.part1}${contact.part2}${contact.part3}`,
        ...(userType === "seller" && { businessNumber }),
      },
    });
  };

  const isFormComplete =
    userType !== "" &&
    contact.part1.length === 3 &&
    contact.part2.length === 4 &&
    contact.part3.length === 4 &&
    (userType === "buyer" ||
      (userType === "seller" && businessNumber.length > 8));

  return (
    <Layout>
      <div className="relative">
        <div className="absolute right-[-142px] top-[-202px] animate-moveBlob2">
          <Blob_1 />
        </div>
      </div>
      <div className="mt-20 flex min-h-[85vh] flex-col">
        <div className="mb-4">
          <h1 className="my-8 ml-8 text-3xl font-bold">회원가입</h1>
          <div className="ml-4">
            <InputLabel name="서비스를 어떻게 이용하실 계획이신가요?" />
          </div>
          <div className="mb-4 flex justify-around space-x-4">
            <div className="flex items-center space-x-2 rounded-full bg-base px-12 py-4 shadow-md">
              <span>구매자</span>
              <Input
                variant="check"
                type="checkbox"
                name="buyer"
                checked={userType === "buyer"}
                onChange={handleUserTypeChange}
              />
            </div>
            <div className="flex items-center space-x-2 rounded-full bg-base px-12 py-4 shadow-md">
              <span>판매자</span>
              <Input
                variant="check"
                type="checkbox"
                name="seller"
                checked={userType === "seller"}
                onChange={handleUserTypeChange}
              />
            </div>
          </div>

          <div className="h-24">
            {showContact ? (
              <div className="mx-4 animate-fadeInSlideUp">
                <InputLabel name="연락처를 알려주세요." />
                <div className="flex items-center justify-between space-x-2">
                  <Input
                    variant="third"
                    name="연락처1"
                    value={contact.part1}
                    onChange={(e) => handleContactChange(e, "part1")}
                    onKeyDown={(e) => handleBackspace(e, "part1")}
                    ref={part1Ref}
                    maxLength={3}
                  />
                  <span className="font-bold"> - </span>
                  <Input
                    variant="third"
                    name="연락처2"
                    value={contact.part2}
                    onChange={(e) => handleContactChange(e, "part2")}
                    onKeyDown={(e) => handleBackspace(e, "part2")}
                    ref={part2Ref}
                    maxLength={4}
                  />
                  <span className="font-bold"> - </span>
                  <Input
                    variant="third"
                    name="연락처3"
                    value={contact.part3}
                    onChange={(e) => handleContactChange(e, "part3")}
                    onKeyDown={(e) => handleBackspace(e, "part3")}
                    ref={part3Ref}
                    maxLength={4}
                  />
                </div>
              </div>
            ) : (
              <div className="h-24" />
            )}
          </div>

          <div className="h-24">
            {showBusinessNumber && userType === "seller" ? (
              <div className="mx-4 animate-fadeInSlideUp">
                <InputLabel name="사업자 등록 번호를 입력해주세요." />
                <Input
                  name="사업자 등록 번호"
                  value={businessNumber}
                  onChange={(e) =>
                    setBusinessNumber(e.target.value.replace(/\D/g, ""))
                  }
                  maxLength={12}
                  autoComplete="off"
                />
              </div>
            ) : (
              <div className="h-24" />
            )}
          </div>
        </div>
        <div className="mx-4 mt-12 flex flex-col gap-2">
          <Button
            content="다음"
            disabled={!isFormComplete}
            onClick={handleNext}
          />
          <Button
            variant="secondary"
            content="취소"
            onClick={() => navigate("/")}
          />
        </div>
      </div>
    </Layout>
  );
};

export default Signup;
