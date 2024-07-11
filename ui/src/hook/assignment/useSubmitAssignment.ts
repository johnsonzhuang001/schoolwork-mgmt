import { useMutation } from "@tanstack/react-query";
import httpClient from "../../http/httpClient";
import queryClient from "../../query/queryClient";
import { QueryKey } from "../../query/queryKey";
import { QuestionAnswer } from "../../type/Assignment";
import { useNotification } from "react-easy-notification";

interface SubmitAssignmentRequest {
  id: number;
  questions: Array<{
    id: number;
    answer: QuestionAnswer | null;
  }>;
}

export const useSaveAssignment = () => {
  const { pushNotification } = useNotification();
  const { mutate, isPending } = useMutation<
    unknown,
    unknown,
    SubmitAssignmentRequest
  >({
    mutationFn: (request) => httpClient.post("/api/assignment/save", request),
    onSuccess: (_, request) => {
      Promise.all([
        queryClient.invalidateQueries({
          queryKey: [QueryKey.ASSIGNMENTS],
        }),
        queryClient.invalidateQueries({
          queryKey: [QueryKey.ASSIGNMENT, request.id],
        }),
      ]).then(() => {
        pushNotification({
          text: "Successfully saved the progress.",
          type: "success",
        });
      });
    },
    onError: () => {
      pushNotification({
        text: "Failed to save the progress",
        type: "danger",
      });
    },
  });

  return {
    saveAssignment: mutate,
    saving: isPending,
  };
};
