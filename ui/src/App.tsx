import React from "react";
import { RecoilRoot } from "recoil";
import { QueryClientProvider } from "@tanstack/react-query";
import queryClient from "./query/queryClient";
import Root from "./navigation/Root";
import { NotificationProvider, Notifications } from "react-easy-notification";

function App() {
  return (
    <RecoilRoot>
      <QueryClientProvider client={queryClient}>
        <NotificationProvider>
          <Root />
          <Notifications timeout={5000} position="bottomCenter" />
        </NotificationProvider>
      </QueryClientProvider>
    </RecoilRoot>
  );
}

export default App;
