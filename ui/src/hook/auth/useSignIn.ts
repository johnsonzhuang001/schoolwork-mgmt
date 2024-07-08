import httpClient from "../../http/httpClient";
import useAccessToken from "./useAccessToken";
import queryClient from "../../query/queryClient";
import { QueryKey } from "../../query/queryKey";

interface SignInRequest {
  username: string;
  password: string;
}

const useSignIn = () => {
  const { persistJwt } = useAccessToken();
  const signin = async (request: SignInRequest) => {
    return httpClient
      .post<SignInRequest, string>("/api/auth/signin", request)
      .then((accessToken) => {
        persistJwt(accessToken);
        queryClient.invalidateQueries({ queryKey: [QueryKey.USER_SELF] });
      });
  };

  return {
    signin,
  };
};

export default useSignIn;
