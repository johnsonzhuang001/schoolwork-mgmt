import React from "react";
import MainBox from "./MainBox";
import Text from "@/components/Text";


const Header = () => {
  return (
    <div className="header top-0 left-0 right-0 w-full flex justify-center min-h-[80px] h-[80px] z-10 shadow-header">
      <MainBox className="content h-full relative flex justify-center items-center">
          <Text type="white" weight="thin" size="lg">Hacker World</Text>
      </MainBox>
    </div>
  );
};

export default Header;
