import Text from "./Text";
import { IoArrowBackOutline } from "react-icons/io5";
import React from "react";
import { useNavigate } from "react-router-dom";

const Back = () => {
  const navigate = useNavigate();
  return (
    <div
      className="w-fit p-[5px] rounded-[6px] border-[1px] border-secondary cursor-pointer opacity-90 hover:opacity-70 transition-opacity duration-300"
      onClick={() => navigate("/")}
    >
      <Text type="secondary">
        <IoArrowBackOutline size="20px" />
      </Text>
    </div>
  );
};

export default Back;
