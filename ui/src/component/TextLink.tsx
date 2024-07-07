import React from "react";
import { Link } from "react-router-dom";
import Text from "./Text";

interface TextLinkProps {
  to: string;
  children: string;
  size?: "lg" | "base" | "sm" | "xs";
  type?: "link" | "secondary";
}

const TextLink: React.FC<TextLinkProps> = ({
  to,
  children,
  size = "base",
  type = "link",
}) => {
  return (
    <Link className="hover:opacity-60 transition-opacity" to={to}>
      <Text size={size} type={type}>
        {children}
      </Text>
    </Link>
  );
};

export default TextLink;
