import React from "react";
import Loading from "./Loading";
import { UserDto, UserRole } from "../type/User";
import Text from "./Text";
import { Tooltip } from "@mui/material";
import { IoPersonOutline } from "react-icons/io5";
import StudentAvatar from "../asset/student_avatar.png";
import MentorAvatar from "../asset/mentor_avatar.png";

interface AvatarProps {
  user: UserDto | null;
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
          <img
            alt="Avatar Image"
            className="w-full h-full object-cover"
            src={user.role === UserRole.MENTOR ? MentorAvatar : StudentAvatar}
          />
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
