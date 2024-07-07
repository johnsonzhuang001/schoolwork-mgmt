import { useRecoilState } from "recoil";
import httpClient from "../http/httpClient";
import { StorageKey } from "../constant/localStorage";
import accessTokenState from "../state/accessTokenState";

interface JwtRequest {
  accessToken: string | null;
}

interface JwtResponse {
  accessToken: string;
}

const useAccessToken = () => {
  const [accessToken, setAccessToken] = useRecoilState(accessTokenState);

  // I'm not using useEffect here to avoid frequent read/write of localStorage

  const persistJwt = (accessToken: string) => {
    localStorage.setItem(StorageKey.ACCESS_TOKEN, accessToken);
    setAccessToken(accessToken);
  };

  const refreshJwt = async () => {
    try {
      const res = await httpClient.post<JwtRequest, JwtResponse>(
        "/api/auth/jwt/refresh",
        {
          accessToken,
        }
      );
      persistJwt(res.accessToken);
    } catch (err) {
      clearJwt();
      throw err;
    }
  };

  const isJwtValid = async () => {
    try {
      const isValid = await httpClient.post<JwtRequest, boolean>(
        "/api/auth/jwt/validate",
        {
          accessToken,
        }
      );
      if (!isValid) {
        clearJwt();
      }
      return isValid;
    } catch {
      clearJwt();
      return false;
    }
  };

  const clearJwt = () => {
    localStorage.removeItem(StorageKey.ACCESS_TOKEN);
    setAccessToken(null);
  };

  return {
    accessToken,
    persistJwt,
    refreshJwt,
    isJwtValid,
    clearJwt,
  };
};

export default useAccessToken;
