import { useQuery } from "@tanstack/react-query";
import { AssignmentDto } from "../../type/Assignment";
import { QueryKey } from "../../query/queryKey";
import httpClient from "../../http/httpClient";

const useAssignments = () => {
  const { data, isLoading } = useQuery<ReadonlyArray<AssignmentDto>>({
    queryKey: [QueryKey.ASSIGNMENTS],
    queryFn: () => httpClient.get("/api/assignment/all"),
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
