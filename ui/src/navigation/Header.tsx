import React from "react";
import Logo from "../component/Logo";
import MainBox from "../component/MainBox";
import useSelf from "../hook/user/useSelf";
import Avatar from "../component/Avatar";

const Profile = () => {
  const { self, isLoading } = useSelf();
  return (
    <div
      className={`user absolute sm:right-[10px] right-0 ${
        !!self ? "cursor-pointer" : ""
      }`}
    >
      <Avatar size={40} user={self} loading={isLoading} />
    </div>
  );
};

const Header = () => {
  return (
    <div className="header top-0 left-0 right-0 w-full bg-white flex justify-center min-h-[80px] h-[80px] z-10 shadow-header">
      <MainBox className="content h-full relative flex justify-center items-center">
        <Logo link className="absolute sm:left-[10px] left-0" />
        <Profile />
      </MainBox>
    </div>
  );
};

export default Header;
