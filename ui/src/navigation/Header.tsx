import React from "react";
import Logo from "../component/Logo";
import MainBox from "../component/MainBox";

const Header = () => {
  return (
    <div className="header top-0 left-0 right-0 w-full bg-white flex justify-center min-h-[80px] h-[80px] z-10 shadow-header">
      <MainBox className="content h-full relative flex justify-center items-center">
        <Logo link className="absolute sm:left-[10px] left-0" />
      </MainBox>
    </div>
  );
};

export default Header;
