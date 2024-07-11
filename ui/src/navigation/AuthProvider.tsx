import React, { ReactNode, useEffect } from "react";
import useAccessToken from "../hook/auth/useAccessToken";
import { useNavigate } from "react-router-dom";

interface AuthProviderProps {
  children: ReactNode;
}

const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const navigate = useNavigate();
  const { accessToken, refreshJwt } = useAccessToken();
  useEffect(() => {
    if (accessToken) {
      refreshJwt().catch(() => {
        navigate("/signin");
      });
    }
  }, [accessToken]);
  return <div>{children}</div>;
};

export default AuthProvider;
