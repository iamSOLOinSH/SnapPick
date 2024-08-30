import React from "react";
import clsx from "clsx";

import { FaCheck } from "react-icons/fa6";

type ProgressStepsProps = {
  currentStep: number;
  steps: number;
};

export const ProgressSteps: React.FC<ProgressStepsProps> = ({
  currentStep,
  steps,
}) => {
  return (
    <div className="flex w-2/3 items-center justify-between">
      {Array.from({ length: steps }, (_, index) => {
        const stepNumber = index + 1;
        const isCompleted = stepNumber < currentStep;
        const isCurrent = stepNumber === currentStep;

        return (
          <React.Fragment key={stepNumber}>
            <div
              className={clsx(
                "flex h-10 w-10 items-center justify-center rounded-full",
                {
                  "bg-primary text-white": isCurrent,
                  "bg-darkBase text-black": !isCompleted && !isCurrent,
                  "border-2 border-base bg-darkBase text-black": isCompleted,
                },
              )}
            >
              {isCurrent ? <FaCheck /> : <span>{stepNumber}</span>}
            </div>
            {stepNumber < steps && (
              <div className="mx-2 h-0.5 flex-1 bg-darkBase" />
            )}
          </React.Fragment>
        );
      })}
    </div>
  );
};
