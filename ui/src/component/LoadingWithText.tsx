import Loading from "./Loading";
import Text from "./Text";
import React from "react";

interface LoadingWithTextProps {
  text: string;
}

const LoadingWithText: React.FC<LoadingWithTextProps> = ({ text }) => {
  return (
    <div className="w-full sm:h-[300px] h-[150px] flex justify-center items-center gap-[5px]">
      <Loading color="secondary" size="18px" />
      <Text type="secondary" size="sm">
        {text}
      </Text>
    </div>
  );
};

export default LoadingWithText;
