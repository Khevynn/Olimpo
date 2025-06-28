import { useMutation} from "@tanstack/react-query";
import axios, { AxiosError } from "axios";
import { useState } from "react";

export const UseAuth: any = (url: string) => {
  const [status, setStatus] = useState({
    success: false,
    message: "",
  })
  const [data, setData] = useState("")

  const {mutate, isPending} = useMutation({
    mutationFn: (data) => axios.post(url, data).then((res) => res.data),
    onSuccess: (data) => {
      setStatus({
        success: true,
        message: data.message,
      })
      setData(data)
    },
    onError: (error: AxiosError) => {
      const axiosError = error as AxiosError<{message: string}>

      setStatus({
        success: false,
        message: axiosError?.message === "Network Error"
        ? "Erro de rede. Por favor, tente mais tarde."
        : axiosError?.response?.data?.message || "Ocorreu um erro inesperado."

      })
    },
  })

  return {status, mutate, isPending, data}
}

