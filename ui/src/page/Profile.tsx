import useSelf from "../hook/user/useSelf";
import MainBox from "../component/MainBox";
import Avatar from "../component/Avatar";
import React, { useState } from "react";
import Button from "../component/Button";
import Text from "../component/Text";
import Input from "../component/Input";
import Back from "../component/Back";
import useSignOut from "../hook/auth/useSignOut";
import { useNavigate } from "react-router-dom";
import { UserDto } from "../type/User";

interface ProfileProps {
  user: UserDto;
  readonly: boolean;
}

const Profile: React.FC<ProfileProps> = ({ user, readonly }) => {
  const [editingProfile, setEditingProfile] = useState(false);
  const [nickname, setNickname] = useState(user?.nickname ?? "");
  const [biography, setBiography] = useState(user?.biography ?? "");
  const { signout } = useSignOut();
  const navigate = useNavigate();
  const profileButtons = () => {
    return (
      <>
        {!readonly && !editingProfile && (
          <>
            <Button
              fullWidth
              type="outline"
              text="Edit Profile"
              onClick={() => {
                setEditingProfile(true);
              }}
            />
          </>
        )}
        {!readonly && editingProfile && (
          <>
            {/*<Button*/}
            {/*    fullWidth*/}
            {/*    loading={updatingProfile}*/}
            {/*    text="Save"*/}
            {/*    disabled={!profileUpdated}*/}
            {/*    onClick={submitProfileUpdate}*/}
            {/*/>*/}
            {/*<Button*/}
            {/*    fullWidth*/}
            {/*    loading={updatingProfile}*/}
            {/*    type="outline"*/}
            {/*    color="red"*/}
            {/*    text="Cancel"*/}
            {/*    onClick={() => {*/}
            {/*        setEditingProfile(false);*/}
            {/*    }}*/}
            {/*/>*/}
          </>
        )}
        {!readonly && !editingProfile && (
          <>
            <Button
              fullWidth
              type="outline"
              color="red"
              text="Sign Out"
              onClick={() => {
                signout();
                navigate("/");
              }}
            />
          </>
        )}
      </>
    );
  };
  return (
    <MainBox className="flex gap-[10px]">
      <div className="profile sm:w-[300px] w-full p-[15px] rounded-[6px] bg-white flex flex-col items-center gap-[10px]">
        <div className="w-full">
          <Back />
        </div>
        <Avatar user={user} size={40} />
        <div className="username w-full flex flex-col items-center">
          {!editingProfile && (
            <Text
              type={user?.nickname ? "primary" : "secondary"}
              inline={false}
            >
              {user?.nickname ?? "Nick Name"}
            </Text>
          )}
          {editingProfile && (
            <Input
              type="text"
              inputClassName="w-full"
              value={nickname}
              onChange={setNickname}
              title="Nick Name"
            />
          )}
          <Text type="secondary" size="sm" inline={false}>
            {user?.username}
          </Text>
        </div>
        <div className="bio w-full">
          {!editingProfile && (
            <Text
              type={user?.biography ? "primary" : "secondary"}
              inline={false}
              wrap="prewrap"
              size="sm"
              className="text-center"
            >
              {user?.biography ??
                (readonly
                  ? "This person has not left any word here..."
                  : "Please add your biography here.")}
            </Text>
          )}
          {editingProfile && (
            <Input
              inputClassName="h-[100px]"
              type="textarea"
              value={biography}
              onChange={setBiography}
              title="Biography"
            />
          )}
        </div>
        {profileButtons()}
      </div>
      <div className="dashboard grow p-[15px] rounded-[6px] bg-white"></div>
    </MainBox>
  );
};

export default Profile;
