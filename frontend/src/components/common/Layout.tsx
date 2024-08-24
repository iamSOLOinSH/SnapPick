import React from "react";

type LayoutProps = {
  children: React.ReactNode;
};

export const Layout = ({ children }: LayoutProps) => {
  return (
    <div className="flex min-h-screen items-center justify-center bg-base">
      <div className="mx-auto w-full max-w-md border-l-2 border-r-2 bg-white p-4">
        {children}
      </div>
    </div>
  );
};
