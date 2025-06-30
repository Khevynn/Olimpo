import { useMutation, useQuery } from "@tanstack/react-query";
import axios from "axios";

export const useProfile = () => {
  const { data, error, isLoading } = useQuery({
    queryKey: ["profile"],
    queryFn: async ()  => {
      return await axios
        //@ts-ignore auth fetch
        .get(import.meta.env.VITE_API_PROFILE, {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("token")}`,
          },
        })
        .then((response) => response.data);
    },
    refetchInterval: 10 * 1000, //10s
    refetchOnWindowFocus: true,
  });
  return { data, error, isLoading };
};


