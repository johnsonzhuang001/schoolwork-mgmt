import React from "react";
import { RecoilRoot } from "recoil";
import { QueryClientProvider } from "@tanstack/react-query";
import queryClient from "./query/queryClient";
import Root from "./navigation/Root";

function App() {
  return (
    <RecoilRoot>
      <QueryClientProvider client={queryClient}>
        <Root />
      </QueryClientProvider>
    </RecoilRoot>
  );
}

export default App;
