"use client";

import { RecoilRoot } from "recoil";
import { QueryClientProvider } from "@tanstack/react-query";
import queryClient from "@/query/queryClient";
import Body from "@/app/body";

export default function Home() {
  return (
    <RecoilRoot>
      <QueryClientProvider client={queryClient}>
        <Body />
      </QueryClientProvider>
    </RecoilRoot>
  );
}
