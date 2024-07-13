import useAccessToken from "./useAccessToken";
import queryClient from "../../query/queryClient";

const useSignOut = () => {
  const { clearJwt } = useAccessToken();

  const signout = () => {
    clearJwt();
    queryClient.clear();
  };

  return {
    signout,
  };
};

export default useSignOut;
