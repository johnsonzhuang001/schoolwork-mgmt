import { useMutation } from "@tanstack/react-query";
import httpClient from "../../http/httpClient";
import { UserDto } from "../../type/User";
import queryClient from "../../query/queryClient";
import { QueryKey } from "../../query/queryKey";
import { useNotification } from "react-easy-notification";

interface ProfileUpdateRequest {
  username: string;
  nickname: string;
  biography: string | null;
}

const useUpdateProfile = () => {
  const { pushNotification } = useNotification();
  const { mutate, isPending, error } = useMutation<
    Readonly<UserDto>,
    unknown,
    ProfileUpdateRequest
  >({
    mutationFn: (request) => httpClient.post("/api/user/profile", request),
    onSuccess: (user) => {
      return Promise.all([
        queryClient.invalidateQueries({
          queryKey: [QueryKey.USER, user.username],
        }),
        queryClient.invalidateQueries({ queryKey: [QueryKey.USER_SELF] }),
        queryClient.invalidateQueries({ queryKey: [QueryKey.USER_GROUP] }),
      ]).then(() => {
        pushNotification({
          text: "Successfully updated the profile.",
          type: "success",
        });
      });
    },
    onError: () => {
      pushNotification({
        text: "Failed to update the profile.",
        type: "danger",
      });
    },
  });

  return {
    updateProfile: mutate,
    isLoading: isPending,
    error,
  };
};

export default useUpdateProfile;
