import useSelf from "../hook/user/useSelf";
import Profile from "./Profile";

const SelfProfile = () => {
  const { self } = useSelf();
  if (!self) return null;
  return <Profile user={self} readonly={false} />;
};

export default SelfProfile;
