import React from "react";
import clsx from "clsx";
import { BottomTab } from "./BottomTab";

type LayoutProps = {
  children: React.ReactNode;
  className?: string;
};

export const Layout = ({ children, className }: LayoutProps) => {
  return (
    <div className="flex flex-col items-center justify-center bg-base">
      <div
        className={clsx(
          "mx-auto mb-16 min-h-screen w-full max-w-md overflow-hidden border-l-2 border-r-2 p-4",
          className,
        )}
      >
        {children}
      </div>
      <BottomTab />
    </div>
  );
};
