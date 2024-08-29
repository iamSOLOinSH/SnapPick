import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import { Layout } from "../components/common/Layout";
import { ProgressSteps } from "../components/common/ProgressSteps";
import { Button } from "../components/common/Button";
import { useBoundStore } from "../store/store";
import { IoChevronBack } from "react-icons/io5";
import StoreCreateStep1 from "../components/storeCreate/StoreCreateStep1";
import StoreCreateStep2 from "../components/storeCreate/StoreCreateStep2";
import StoreCreateStep3 from "../components/storeCreate/StoreCreateStep3";

const StoreCreate = () => {
  const [step, setStep] = useState(1);
  const navigate = useNavigate();
  const [isFormComplete, setIsFormComplete] = useState(false);
  const { submitStoreCreation } = useBoundStore();

  const handleNextStep = () => {
    if (step < 3) {
      setStep(step + 1);
      setIsFormComplete(false);
    } else {
      submitStoreCreation();
      navigate("/store/create/success");
    }
  };

  const handlePrevStep = () => {
    if (step > 1) {
      setStep(step - 1);
    }
  };

  const renderStepComponent = () => {
    switch (step) {
      case 1:
        return <StoreCreateStep1 setIsFormComplete={setIsFormComplete} />;
      case 2:
        return <StoreCreateStep2 setIsFormComplete={setIsFormComplete} />;
      case 3:
        return <StoreCreateStep3 setIsFormComplete={setIsFormComplete} />;
      default:
        return null;
    }
  };

  return (
    <Layout>
      <div className="flex min-h-screen flex-col">
        {step > 1 && (
          <div>
            <button
              className="rounded border-2 p-2 hover:bg-base"
              onClick={handlePrevStep}
            >
              <IoChevronBack />
            </button>
          </div>
        )}
        <div className="flex justify-end p-4">
          <ProgressSteps currentStep={step} steps={3} />
        </div>
        <h1 className="mb-4 mt-4 text-2xl font-bold">
          {step === 1 && "스토어 기본 정보를"}
          {step === 2 && "스토어 위치 및 운영 정보를"}
          {step === 3 && "스토어 추가 정보를"}
          <br />
          입력해주세요.
        </h1>
        <div className="">{renderStepComponent()}</div>
        <div className="mt-4">
          <Button
            content={step === 3 ? "완료" : "다음"}
            onClick={handleNextStep}
            disabled={!isFormComplete}
          />
        </div>
      </div>
    </Layout>
  );
};

export default StoreCreate;
