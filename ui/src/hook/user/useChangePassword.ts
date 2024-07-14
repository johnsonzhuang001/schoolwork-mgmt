import { useMutation } from "@tanstack/react-query";
import httpClient from "../../http/httpClient";
import { UserDto } from "../../type/User";
import { useNotification } from "react-easy-notification";

interface ChangePasswordRequest {
  password: string;
}

const useChangePassword = () => {
  const { pushNotification } = useNotification();
  const { mutate, isPending } = useMutation<
    Readonly<UserDto>,
    unknown,
    ChangePasswordRequest
  >({
    mutationFn: (request) =>
      httpClient.post("/api/user/password/change", request),
    onSuccess: () => {
      pushNotification({
        text: "Successfully changed the password.",
        type: "success",
      });
    },
    onError: () => {
      pushNotification({
        text: "Failed to change the password.",
        type: "danger",
      });
    },
  });

  return {
    changePassword: mutate,
    changing: isPending,
  };
};

export default useChangePassword;
