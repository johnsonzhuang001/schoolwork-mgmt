import useAccessToken from "./useAccessToken";

const useAuthenticatedQueryFn = <T>(func: () => Promise<T>) => {
  const { accessToken } = useAccessToken();
  if (!accessToken) {
    return () => null;
  } else {
    return func;
  }
};

export default useAuthenticatedQueryFn;
