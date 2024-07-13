import { useQuery } from "@tanstack/react-query";
import { UserDto } from "../../type/User";
import { QueryKey } from "../../query/queryKey";
import httpClient from "../../http/httpClient";

const useStudyGroup = () => {
  const { data, isLoading } = useQuery<ReadonlyArray<UserDto> | null>({
    queryKey: [QueryKey.USER_GROUP],
    queryFn: () => httpClient.get("/api/user/group"),
    gcTime: 3600000,
    staleTime: 1800000,
    retry: 0,
  });

  return {
    studyGroup: data ?? [],
    isLoading,
  };
};

export default useStudyGroup;
