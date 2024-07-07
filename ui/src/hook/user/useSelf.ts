import { useQuery } from "@tanstack/react-query";
import { QueryKey } from "../../query/queryKey";
import httpClient from "../../http/httpClient";
import { SelfDto } from "../../type/User";
import useAuthenticatedQueryFn from "../useAuthenticatedQueryFn";

const useSelf = () => {
  const { data, isLoading, error } = useQuery<Readonly<SelfDto> | null>({
    queryKey: [QueryKey.USER_SELF],
    queryFn: useAuthenticatedQueryFn(() => httpClient.get("/user/self")),
    gcTime: 3600000,
    staleTime: 1800000,
    retry: 0,
  });

  return {
    self: data ?? null,
    isLoading,
    error,
  };
};

export default useSelf;
