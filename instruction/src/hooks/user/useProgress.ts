import useAccessToken from "@/hooks/auth/useAccessToken";
import { useQuery } from "@tanstack/react-query";
import { ProgressDto } from "@/type/User";
import { QueryKey } from "@/query/queryKey";
import useAuthenticatedQueryFn from "@/hooks/auth/useAuthenticatedQueryFn";
import httpClient from "@/http/httpClient";

const useProgress = () => {
  const { accessToken, clearAccessToken } = useAccessToken();
  const { data, isLoading, error } = useQuery<Readonly<ProgressDto> | null>({
    queryKey: [QueryKey.PROGRESS],
    queryFn: useAuthenticatedQueryFn(() =>
      httpClient.get("/instruction/progress"),
    ),
    gcTime: 3600000,
    staleTime: 1800000,
    retry: 0,
    enabled: !!accessToken,
  });

  if (error) {
    clearAccessToken();
  }

  return {
    progress: data ?? null,
    loadingProgress: isLoading,
    error,
  };
};

export default useProgress;
