import { useMutation } from "@tanstack/react-query";
import httpClient from "../../http/httpClient";
import queryClient from "../../query/queryClient";
import { QueryKey } from "../../query/queryKey";
import { useNotification } from "react-easy-notification";

const useWithdrawAssignment = () => {
  const { pushNotification } = useNotification();
  const { mutate, isPending } = useMutation<unknown, unknown, number>({
    mutationFn: (assignmentId) =>
      httpClient.post(`/api/assignment/withdraw/${assignmentId}`),
    onSuccess: (_, assignmentId) => {
      Promise.all([
        queryClient.invalidateQueries({
          queryKey: [QueryKey.ASSIGNMENTS],
        }),
        queryClient.invalidateQueries({
          queryKey: [QueryKey.ASSIGNMENT, assignmentId],
        }),
      ]).then(() => {
        pushNotification({
          text: "Successfully withdrew the submission.",
          type: "success",
        });
      });
    },
    onError: () => {
      pushNotification({
        text: "Failed to withdraw the submission",
        type: "danger",
      });
    },
  });

  return {
    withdrawSubmission: mutate,
    withdrawing: isPending,
  };
};

export default useWithdrawAssignment;
