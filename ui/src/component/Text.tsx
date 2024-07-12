import React, { ReactNode } from "react";
import { TextColor } from "../type/Color";

interface TextProps {
  type?: TextColor;
  size?: "banner" | "sub-banner" | "xl" | "lg" | "base" | "sm" | "xs";
  pointer?: boolean;
  weight?: "bold" | "normal" | "light";
  inline?: boolean;
  children: ReactNode;
  onClick?: () => void;
  className?: string;
  wrap?: "default" | "prewrap";
}

const Text: React.FC<TextProps> = ({
  type = "primary",
  size = "base",
  pointer = false,
  weight = "base",
  inline = true,
  onClick,
  children,
  className = "",
  wrap = "default",
}) => {
  const getHoverColor = () => {
    switch (type) {
      case "primary":
        return "secondary";
      case "secondary":
        return "primary";
      default:
        return "secondary";
    }
  };
  const getWrapClass = () => {
    switch (wrap) {
      case "prewrap":
        return "whitespace-pre-wrap";
      case "default":
        return "";
    }
  };
  const getFontSizeClass = () => {
    switch (size) {
      case "xs":
        return "text-xs";
      case "sm":
        return "mobile:text-sm text-xs";
      case "base":
        return "mobile:text-base text-sm";
      case "lg":
        return "mobile:text-lg text-base";
      case "xl":
        return "mobile:text-xl text-lg";
      case "sub-banner":
        return "mobile:text-[40px] text-[32px]";
      case "banner":
        return "mobile:text-[60px] text-[46px]";
      default:
        return "text-base";
    }
  };
  const getColorClass = () => {
    switch (type) {
      case "primary":
        return "text-primary";
      case "secondary":
        return "text-secondary";
      case "danger":
        return "text-red";
      case "link":
        return "text-blue";
      case "white":
        return "text-white";
      case "warning":
        return "text-yellow";
      case "success":
        return "text-green";
      default:
        return "text-primary";
    }
  };
  const getWeightClass = () => {
    switch (weight) {
      case "bold":
        return "font-bold";
      case "base":
        return "";
      case "light":
        return "font-light";
      default:
        return "font-normal";
    }
  };
  const textClassName = `${getColorClass()} ${getWrapClass()} ${getFontSizeClass()} ${getWeightClass()} ${
    pointer ? `cursor-pointer hover:text-${getHoverColor()}` : ""
  } ${className}`;
  return inline ? (
    <span className={textClassName} onClick={onClick}>
      {children}
    </span>
  ) : (
    <p className={textClassName} onClick={onClick}>
      {children}
    </p>
  );
};

export default Text;
