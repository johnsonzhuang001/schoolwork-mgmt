import { Outlet, useLocation } from "react-router-dom";
import React from "react";
import Header from "./Header";

const Layout = () => {
  const location = useLocation();
  const isCommunity = location.pathname.includes("/community/");
  return (
    <div
      className={`relative w-full ${
        isCommunity ? "max-h-[100vh] h-[100vh]" : "min-h-[100vh]"
      } flex flex-col bg-almostwhite overflow-hidden`}
    >
      <Header />
      <div className="grow flex justify-center overflow-auto">
        <Outlet />
      </div>
    </div>
  );
};

export default Layout;
