import { useQuery } from "@tanstack/react-query";
import { QueryKey } from "../../query/queryKey";
import httpClient from "../../http/httpClient";
import { UserDto } from "../../type/User";

const useUser = (username: string) => {
  const { data, isLoading } = useQuery<Readonly<UserDto>>({
    queryKey: [QueryKey.USER, username],
    queryFn: () => httpClient.get(`/api/user/username/${username}`),
    gcTime: 3600000,
    staleTime: 1800000,
    retry: 0,
  });

  return {
    user: data ?? null,
    isLoading,
  };
};

export default useUser;
