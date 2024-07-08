import { AiOutlineLoading } from "react-icons/ai";
import React from "react";
import { TextColor } from "../type/Color";
import Text from "./Text";

interface LoadingProps {
  size?: string;
  color?: TextColor;
}

const Loading: React.FC<LoadingProps> = ({ size, color = "primary" }) => (
  <Text type={color}>
    <AiOutlineLoading size={size} className="animate-spin" />
  </Text>
);

export default Loading;
