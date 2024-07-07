import { atom } from "recoil";
import { StorageKey } from "../constant/localStorage";

const accessTokenState = atom<string | null>({
  key: "accessToken",
  default: localStorage.getItem(StorageKey.ACCESS_TOKEN),
});

export default accessTokenState;
