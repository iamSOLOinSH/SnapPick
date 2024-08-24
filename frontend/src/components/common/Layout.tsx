import React from "react";
import { BottomTab } from "./BottomTab";

type LayoutProps = {
  children: React.ReactNode;
};

export const Layout = ({ children }: LayoutProps) => {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center bg-base">
      <div className="mx-auto mb-16 w-full max-w-md border-l-2 border-r-2 bg-white p-4">
        {children}
      </div>
      <BottomTab />
    </div>
  );
};
