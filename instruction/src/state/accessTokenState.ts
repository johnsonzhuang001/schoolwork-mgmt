import { atom } from "recoil";
import { StorageKey } from "@/constant/localStorage";

const accessTokenState = atom<string | null>({
  key: "accessToken",
  default:
    typeof window !== "undefined"
      ? localStorage.getItem(StorageKey.ACCESS_TOKEN)
      : null,
});

export default accessTokenState;
