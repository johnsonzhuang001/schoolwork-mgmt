import httpClient from "../../http/httpClient";
import useAccessToken from "./useAccessToken";
import queryClient from "../../query/queryClient";
import { QueryKey } from "../../query/queryKey";
import sha256 from "crypto-js/sha256";
import Base64 from "crypto-js/enc-base64";
import UTF8 from "crypto-js/enc-utf8";

interface SignInRequest {
  username: string;
  password: string;
}

const useSignIn = () => {
  const { persistAccessToken } = useAccessToken();
  const signin = async (request: SignInRequest) => {
    return httpClient
      .post<SignInRequest, string>("/api/auth/signin", request)
      .then(() => {
        const hashDigest = sha256(request.username);
        persistAccessToken(
          Base64.stringify(
            UTF8.parse(
              JSON.stringify({
                username: request.username,
                hash: hashDigest.toString(),
              })
            )
          )
        );
        queryClient.invalidateQueries({ queryKey: [QueryKey.USER_SELF] });
      });
  };

  return {
    signin,
  };
};

export default useSignIn;
