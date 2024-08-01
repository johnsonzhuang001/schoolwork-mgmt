import useAccessToken from "./useAccessToken";
import queryClient from "../../query/queryClient";

const useSignOut = () => {
  const { clearAccessToken } = useAccessToken();

  const signout = () => {
    clearAccessToken();
    queryClient.clear();
  };

  return {
    signout,
  };
};

export default useSignOut;
