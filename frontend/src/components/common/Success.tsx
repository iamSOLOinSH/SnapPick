import React, { useEffect } from "react";
import { FaCheck } from "react-icons/fa6";

export const Success = () => {
  useEffect(() => {
    const confettiPieces = document.querySelectorAll(
      ".confetti-piece",
    ) as NodeListOf<HTMLElement>;

    confettiPieces.forEach((piece) => {
      const angle = Math.random() * Math.PI * 2;
      const horizontalBias = Math.cos(angle) * (80 + Math.random() * 100);
      const verticalBias = Math.sin(angle) * (70 + Math.random() * 60);
      const rotation = Math.random() * 360;

      piece.style.setProperty("--x", `${horizontalBias}px`);
      piece.style.setProperty("--y", `${verticalBias}px`);
      piece.style.setProperty("--rotate", `${rotation}deg`);
    });
  }, []);

  return (
    <div className="relative inset-0 flex items-center justify-center">
      {/* 중앙 8각형 체크모양 */}
      <div className="animate-octagonAppear relative z-50 flex items-center justify-center">
        <div
          className="flex h-24 w-24 items-center justify-center bg-primary text-white"
          style={{
            clipPath:
              "polygon(50% 0%, 85% 15%, 100% 50%, 85% 85%, 50% 100%, 15% 85%, 0% 50%, 15% 15%)",
          }}
        >
          <FaCheck className="h-12 w-12" />
        </div>
      </div>
      {[...Array(26)].map((_, index) => (
        <div
          key={index}
          className="confetti-piece animate-explode absolute rounded-sm bg-gradient-to-br from-primary to-secondary"
          style={{
            width: `${Math.random() * 4 + 2}px`,
            height: `${Math.random() * 4 + 2}px`,
          }}
        ></div>
      ))}
    </div>
  );
};
