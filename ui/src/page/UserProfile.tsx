import { useParams } from "react-router-dom";
import useUser from "../hook/user/useUser";
import Profile from "./Profile";

const UserProfile = () => {
  const { username } = useParams();
  const { user } = useUser(username ?? "");
  if (!user) return null;
  return <Profile user={user} readonly />;
};

export default UserProfile;
