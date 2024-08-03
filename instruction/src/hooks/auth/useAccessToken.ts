import { useRecoilState } from "recoil";
import { StorageKey } from "@/constant/localStorage";
import accessTokenState from "@/state/accessTokenState";

const useAccessToken = () => {
  const [accessToken, setAccessToken] = useRecoilState(accessTokenState);

  const persistAccessToken = (accessToken: string) => {
    localStorage.setItem(StorageKey.ACCESS_TOKEN, accessToken);
    setAccessToken(accessToken);
  };

  const clearAccessToken = () => {
    localStorage.removeItem(StorageKey.ACCESS_TOKEN);
    setAccessToken(null);
  };

  return {
    accessToken,
    persistAccessToken,
    clearAccessToken,
  };
};

export default useAccessToken;
