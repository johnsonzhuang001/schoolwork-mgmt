import React, { useState } from "react";
import Logo from "../../component/Logo";
import Text from "../../component/Text";
import LogoText from "../../component/LogoText";
import MainBox from "../../component/MainBox";
import Button from "../../component/Button";
import Input from "../../component/Input";
import useSignIn from "../../hook/auth/useSignIn";
import { useNavigate } from "react-router-dom";

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

const SignIn = () => {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [errors, setErrors] = useState<Errors>(initialErrors);
  const { signin } = useSignIn();
  const navigate = useNavigate();
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

  const onSubmit = () => {
    if (isValid()) {
      setLoading(true);
      return signin({
        username: username,
        password,
      })
        .then(() => {
          navigate("/");
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
    <MainBox className="flex flex-col items-center py-[30px] gap-[30px]">
      <div className="flex items-end gap-[10px]">
        <span className="sm:inline hidden text-[36px] text-primary">
          Welcome to
        </span>
        <div className="h-[68px]">
          <LogoText />
        </div>
      </div>
      <div className="form sm:w-[500px] w-[300px] h-fit rounded-[10px] bg-white py-[30px] px-[20px] flex flex-col items-center gap-[10px]">
        <div className="title">
          <Text type="secondary" size="lg">
            Sign In to CoolCode Education
          </Text>
        </div>
        <div className="divider w-full h-[1px] bg-whitegray" />
        <div className="w-full flex flex-col gap-[30px]">
          <div className="inputs w-full flex flex-col gap-[5px]">
            <Input
              type="text"
              title="Username"
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
          <Button
            fullWidth
            color="blue"
            text="Sign In"
            rounded
            onClick={onSubmit}
            loading={loading}
          />
        </div>
      </div>
    </MainBox>
  );
};

export default SignIn;
