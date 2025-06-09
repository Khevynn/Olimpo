import { useMutation, UseMutationResult } from "@tanstack/react-query";
import axios, { AxiosError } from "axios";

interface UseAuthMutationParams {
  url: string,
  onSuccessCallback: (data: any) => void,
  onErrorCallback: (success: boolean, message: string) => void
}

export function useAuthMutation({url, onSuccessCallback, onErrorCallback}: UseAuthMutationParams) : UseMutationResult <any, AxiosError, any> {
  return useMutation({
    mutationFn: (data) => axios.post(url, data).then(res => res.data),
    onSuccess: onSuccessCallback,
    onError: (error: AxiosError) => {
      if (error.code === "ERR_BAD_REQUEST") {
        const parsed = JSON.parse(error.request.response);
        onErrorCallback(false, parsed.message);
      } else {
        onErrorCallback(false, "Servidor indisponível. Tente mais tarde");
      }
    }
  })
}