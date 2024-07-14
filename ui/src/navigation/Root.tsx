import React from "react";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import useSelf from "../hook/user/useSelf";
import Layout from "./Layout";
import Home from "../page/Home";
import SignIn from "../page/auth/SignIn";
import Assignment from "../page/Assignment";
import SelfProfile from "../page/SelfProfile";
import UserProfile from "../page/UserProfile";

const Authenticated = ({ children }: { children: JSX.Element }) => {
  const { self, isLoading } = useSelf();
  if (isLoading) return null;
  if (!self) {
    return <Navigate to="/signin" />;
  } else return children;
};

const Root = () => {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/" element={<Layout />}>
          <Route
            index
            element={
              <Authenticated>
                <Home />
              </Authenticated>
            }
          />
          <Route
            path="assignment/:assignmentId"
            element={
              <Authenticated>
                <Assignment />
              </Authenticated>
            }
          />
          <Route
            path="profile"
            element={
              <Authenticated>
                <SelfProfile />
              </Authenticated>
            }
          />
          <Route
            path="profile/:username"
            element={
              <Authenticated>
                <UserProfile />
              </Authenticated>
            }
          />
          <Route path="signin" element={<SignIn />} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
};

export default Root;
