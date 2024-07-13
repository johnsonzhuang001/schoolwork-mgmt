import { UserDto, UserRole } from "../type/User";
import useSelf from "../hook/user/useSelf";
import { useNavigate } from "react-router-dom";
import Text from "./Text";
import { Tooltip } from "@mui/material";
import { AiOutlineMessage } from "react-icons/ai";
import { MdOutlineEmail } from "react-icons/md";

const toReadableRole = (role: UserRole) => {
  switch (role) {
    case UserRole.MENTOR:
      return "Senior Mentor";
    case UserRole.STUDENT:
      return "Student";
    case UserRole.ADMIN:
      return "Admin";
  }
};

const UserCard = ({ user }: { user: UserDto }) => {
  const { self } = useSelf();
  const navigate = useNavigate();
  return (
    <div
      className="user-card relative rounded-[6px] bg-white  overflow-hidden border-[1px] border-whitegray cursor-pointer hover:shadow-card transition-shadow duration-300"
      onClick={() => navigate(`/profile/${user.username}`)}
    >
      <div className="absolute w-full top-0 h-[64px] bg-blue z-0" />
      <div className="relative h-full flex flex-col items-center gap-[10px] pt-[30px] pb-[20px] px-[10px] z-1">
        <div className="w-[60px] h-[60px] rounded-[30px] bg-whitegray" />
        <div className="flex flex-col items-center">
          <Text>{user.nickname}</Text>
          <Text type="secondary" size="xs">
            {toReadableRole(user.role)}
          </Text>
        </div>
        <Text size="sm" className="text-center grow" wrap="prewrap">
          {user.biography || "This person has not left any word here..."}
        </Text>
        {self?.username !== user.username && (
          <div className="flex gap-[10px] items-center bottom-0">
            <Tooltip title="Not implemented yet">
              <div className="cursor-pointer text-secondary hover:opacity-70 transition-opacity duration-300">
                <AiOutlineMessage size="18px" />
              </div>
            </Tooltip>
            <Tooltip title="Not implemented yet">
              <div className="cursor-pointer text-secondary hover:opacity-70 transition-opacity duration-300">
                <MdOutlineEmail size="20px" fontWeight={0} />
              </div>
            </Tooltip>
          </div>
        )}
      </div>
    </div>
  );
};

export default UserCard;
