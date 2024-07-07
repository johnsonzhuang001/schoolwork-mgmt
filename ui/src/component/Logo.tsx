import React from "react";
import { Link } from "react-router-dom";
import logo from "../asset/logo_600.png";

const Logo = ({
  link,
  size = "normal",
  className,
}: {
  link: boolean;
  size?: "lg" | "normal" | "sm";
  className?: string;
}) => {
  const getMdWidth = () => {
    switch (size) {
      case "lg":
        return "md:w-[250px]";
      case "normal":
        return "md:w-[200px]";
      case "sm":
        return "md:w-[180px]";
    }
  };
  const logoClassName = `logo ${getMdWidth()} w-[180px] ${className}`;
  return link ? (
    <Link to="/" className={logoClassName}>
      <img src={logo} alt="Logo" />
    </Link>
  ) : (
    <div className={logoClassName}>
      <img src={logo} alt="Logo" />
    </div>
  );
};

export default Logo;
