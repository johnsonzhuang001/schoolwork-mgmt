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

const useSubmitAssignment = () => {
  const { pushNotification } = useNotification();
  const { mutate, isPending } = useMutation<
    unknown,
    unknown,
    { request: SubmitAssignmentRequest; readyForScore: boolean }
  >({
    mutationFn: ({ request, readyForScore }) =>
      httpClient.post(
        readyForScore ? "/api/assignment/submit" : "/api/assignment/save",
        request
      ),
    onSuccess: (_, { request, readyForScore }) => {
      Promise.all([
        queryClient.invalidateQueries({
          queryKey: [QueryKey.ASSIGNMENTS],
        }),
        queryClient.invalidateQueries({
          queryKey: [QueryKey.ASSIGNMENT, request.id],
        }),
      ]).then(() => {
        pushNotification({
          text: readyForScore
            ? "Successfully submitted the assignment"
            : "Successfully saved the progress.",
          type: "success",
        });
      });
    },
    onError: (_, { readyForScore }) => {
      pushNotification({
        text: readyForScore
          ? "Failed to submit the assignment"
          : "Failed to save the progress",
        type: "danger",
      });
    },
  });

  return {
    submitAssignment: mutate,
    submitting: isPending,
  };
};

export default useSubmitAssignment;
