"use client";

import React, { useState } from "react";
import Text from "@/components/Text";
import Button from "@/components/Button";
import Input from "@/components/Input";
import useSignUp from "@/hooks/auth/useSignUp";
import { useRouter } from "next/navigation";
import useSignIn from "@/hooks/auth/useSignIn";

interface Errors {
  username: string;
  password: string;
  general: string;
}

const initialErrors: Errors = {
  username: "",
  password: "",
  general: "",
};

const SignUpForm = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState<Errors>(initialErrors);
  const { signup } = useSignUp();
  const { signin } = useSignIn();
  const router = useRouter();
  const [loading, setLoading] = useState(false);

  const isValid = () => {
    let valid = !!(username && password);
    setErrors(() => {
      return {
        username: username ? "" : "Required",
        password: password ? "" : "Required",
        general: "",
      };
    });
    return valid;
  };

  const onSubmit = (isSignUp: boolean) => {
    if (isValid()) {
      setLoading(true);
      const func = isSignUp ? signup : signin;
      return func({
        username: username,
        password,
        nickname: username,
      })
        .then(() => {
          router.push("/");
        })
        .catch((err: Error) => {
          setErrors((prevState) => {
            return {
              ...prevState,
              general: err.message,
            };
          });
        })
        .finally(() => {
          setLoading(false);
        });
    }
  };
  return (
    <div className="w-[380px] max-w-full flex flex-col gap-[30px] bg-lightdark p-[10px] rounded-[6px]">
      <div className="inputs w-full flex flex-col gap-[5px]">
        <Input
          type="text"
          title="Team Name"
          value={username}
          onChange={setUsername}
          error={errors.username}
        />
        <Input
          type="text"
          title="Password"
          masked
          value={password}
          onChange={setPassword}
          error={errors.password}
        />
      </div>
      {errors.general && (
        <div>
          <Text type="danger" size="xs">
            {errors.general}
          </Text>
        </div>
      )}
      <div className="w-full flex flex-col gap-[5px]">
        <Button
          fullWidth
          color="dark"
          text="Sign Up"
          rounded
          onClick={() => onSubmit(true)}
          loading={loading}
        />
        <Button
          fullWidth
          color="dark"
          text="Sign In"
          rounded
          onClick={() => onSubmit(false)}
          loading={loading}
        />
      </div>
    </div>
  );
};

export default SignUpForm;
