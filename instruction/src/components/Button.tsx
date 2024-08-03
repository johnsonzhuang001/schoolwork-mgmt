import React, { ReactNode } from "react";
import { AiOutlineLoading } from "react-icons/ai";
import { TextColor } from "@/type/Color";
import Text from "./Text";

interface ButtonProps {
  type?: "fill" | "outline";
  color?:
    | "primary"
    | "secondary"
    | "blue"
    | "red"
    | "white"
    | "yellow"
    | "green"
    | "dark";
  size?: "lg" | "base" | "sm" | "xs";
  rounded?: boolean;
  text: string;
  onClick?: () => void;
  disabled?: boolean;
  fullWidth?: boolean;
  className?: string;
  loading?: boolean;
  icon?: ReactNode;
}

const Button: React.FC<ButtonProps> = ({
  type = "fill",
  color = "primary",
  size = "sm",
  rounded = true,
  text,
  onClick,
  disabled = false,
  fullWidth = false,
  className,
  loading = false,
  icon,
}) => {
  const getBgClass = () => {
    if (type === "fill") {
      switch (color) {
        case "primary":
          return "bg-primary";
        case "secondary":
          return "bg-secondary";
        case "blue":
          return "bg-blue";
        case "red":
          return "bg-red";
        case "green":
          return "bg-green";
        case "dark":
          return "bg-dark";
      }
    }
    return "";
  };

  const getBorderColor = () => {
    if (type === "outline") {
      switch (color) {
        case "primary":
          return "border-primary";
        case "secondary":
          return "border-secondary";
        case "blue":
          return "border-blue";
        case "red":
          return "border-red";
        case "white":
          return "border-white";
        case "yellow":
          return "border-yellow";
        case "green":
          return "border-green";
      }
    }
    return "";
  };

  const getTextType = (): TextColor => {
    if (type === "outline") {
      switch (color) {
        case "primary":
          return "primary";
        case "secondary":
          return "secondary";
        case "blue":
          return "link";
        case "red":
          return "danger";
        case "white":
          return "white";
        case "yellow":
          return "warning";
        case "green":
          return "success";
      }
    }
    return "white";
  };

  const getPadding = () => {
    switch (size) {
      case "xs":
        return "px-[10px] py-[5px]";
      case "sm":
        return "px-[12px] py-[8px]";
      default:
        return "mobile:px-[20px] mobile:py-[10px] px-[15px] py-[8px]";
    }
  };

  const buttonClassName = `button ${getPadding()} ${
    fullWidth ? "w-full" : "w-fit"
  } h-fit flex justify-center items-center gap-[5px] ${
    !disabled && !loading
      ? "cursor-pointer hover:opacity-70"
      : "cursor-default opacity-70"
  } transition-opacity ${getBgClass()} ${
    type === "outline" ? "border-[1px]" : ""
  } ${getBorderColor()} ${
    rounded ? "rounded-full" : "rounded-[6px]"
  } ${className}`;

  return (
    <div
      className={buttonClassName}
      onClick={() => {
        if (!disabled && !loading) {
          onClick?.();
        }
      }}
    >
      <Text type={getTextType()} size={size}>
        {loading ? <AiOutlineLoading className="animate-spin" /> : icon}
      </Text>
      <Text type={getTextType()} size={size}>
        {text}
      </Text>
    </div>
  );
};

export default Button;
