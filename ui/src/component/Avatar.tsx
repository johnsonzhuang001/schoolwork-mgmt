import React from "react";
import Loading from "./Loading";
import { SelfDto } from "../type/User";
import Text from "./Text";
import { PiStudentFill } from "react-icons/pi";
import { Tooltip } from "@mui/material";
import { IoPersonOutline } from "react-icons/io5";

interface AvatarProps {
  user: SelfDto | null;
  size: number;
  loading?: boolean;
}

// TODO: More fancy picture & differentiate between roles
const Avatar: React.FC<AvatarProps> = ({ user, size, loading = false }) => {
  return (
    <Tooltip title={user?.nickname ?? "Anonymous"}>
      <div
        className="flex justify-center items-center bg-whitegray overflow-hidden"
        style={{
          width: size,
          height: size,
          borderRadius: size / 2,
        }}
      >
        {loading && <Loading color="secondary" />}
        {!loading && user ? (
          <Text size="sub-banner">
            <PiStudentFill size="30px" />
          </Text>
        ) : (
          <Text type="secondary">
            <IoPersonOutline size={size / 2} />
          </Text>
        )}
      </div>
    </Tooltip>
  );
};

export default Avatar;
