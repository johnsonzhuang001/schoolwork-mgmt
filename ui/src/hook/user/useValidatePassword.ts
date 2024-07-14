import { useMutation } from "@tanstack/react-query";
import httpClient from "../../http/httpClient";
import { UserDto } from "../../type/User";

interface ValidatePasswordRequest {
  password: string;
}

const useValidatePassword = () => {
  const { mutate, isPending } = useMutation<
    Readonly<UserDto>,
    unknown,
    ValidatePasswordRequest
  >({
    mutationFn: (request) =>
      httpClient.post("/api/user/password/validate", request),
  });

  return {
    validatePassword: mutate,
    validating: isPending,
  };
};

export default useValidatePassword;
