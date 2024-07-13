import { useQuery } from "@tanstack/react-query";
import { AssignmentDto } from "../../type/Assignment";
import { QueryKey } from "../../query/queryKey";
import httpClient from "../../http/httpClient";

const useAssignments = (username: string) => {
  const { data, isLoading } = useQuery<ReadonlyArray<AssignmentDto>>({
    queryKey: [QueryKey.ASSIGNMENTS, username],
    queryFn: () => httpClient.get(`/api/assignment/all/${username}`),
    gcTime: 3600000,
    staleTime: 1800000,
    retry: 0,
  });

  return {
    assignments: data ?? [],
    isLoading,
  };
};

export default useAssignments;
