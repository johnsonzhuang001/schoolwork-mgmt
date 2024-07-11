import { useQuery } from "@tanstack/react-query";
import { AssignmentWithQuestionsDto } from "../../type/Assignment";
import { QueryKey } from "../../query/queryKey";
import httpClient from "../../http/httpClient";

const useAssignment = (assignmentId: number) => {
  const { data, isLoading } = useQuery<AssignmentWithQuestionsDto>({
    queryKey: [QueryKey.ASSIGNMENT, assignmentId],
    queryFn: () => httpClient.get(`/api/assignment/details/${assignmentId}`),
    gcTime: 3600000,
    staleTime: 1800000,
    retry: 0,
  });

  return {
    assignment: data ?? null,
    isLoading,
  };
};

export default useAssignment;
