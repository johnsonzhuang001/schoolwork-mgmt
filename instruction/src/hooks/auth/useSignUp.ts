import httpClient from "../../http/httpClient";
import useAccessToken from "./useAccessToken";
import queryClient from "@/query/queryClient";
import { QueryKey } from "@/query/queryKey";
import sha256 from "crypto-js/sha256";
import Base64 from "crypto-js/enc-base64";
import UTF8 from "crypto-js/enc-utf8";

interface SignUpRequest {
  username: string;
  password: string;
  nickname: string;
}

const SALT = "coolcodehacker";

const useSignUp = () => {
  const { persistAccessToken } = useAccessToken();
  const signup = async (request: SignUpRequest) => {
    return httpClient
      .post<SignUpRequest, string>("/api/auth/signup", request)
      .then(() => {
        const hashDigest = sha256(request.username + SALT);
        persistAccessToken(
          Base64.stringify(
            UTF8.parse(
              JSON.stringify({
                username: request.username,
                hash: hashDigest.toString(),
              }),
            ),
          ),
        );
        queryClient.invalidateQueries({ queryKey: [QueryKey.USER_SELF] });
        queryClient.invalidateQueries({ queryKey: [QueryKey.PROGRESS] });
      });
  };

  return {
    signup,
  };
};

export default useSignUp;
