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
import useAssignments from "../hook/assignment/useAssignments";
import LoadingWithText from "../component/LoadingWithText";
import AssignmentCard from "../component/AssignmentCard";
import useStudyGroup from "../hook/user/useStudyGroup";
import UserCard from "../component/UserCard";

interface ProfileProps {
  user: UserDto;
  readonly: boolean;
}

const Students = () => {
  const { studyGroup, isLoading } = useStudyGroup();
  return (
    <div className="w-full flex flex-col p-[10px] gap-[10px] bg-white rounded-[6px]">
      <div>
        <Text>Mentees</Text>
      </div>
      {isLoading && (
        <div>
          <LoadingWithText text="Loading assignments..." />
        </div>
      )}
      {!isLoading && (
        <div className="grid lg:grid-cols-4 md:grid-cols-3 grid-cols-2 gap-[10px]">
          {studyGroup
            .filter((user) => user.role === "STUDENT")
            .map((user) => {
              return <UserCard key={user.username} user={user} />;
            })}
        </div>
      )}
    </div>
  );
};

const Assignments = ({ user }: { user: UserDto }) => {
  const { assignments, isLoading } = useAssignments(user.username);
  return (
    <div className="w-full flex flex-col p-[10px] gap-[10px] bg-white rounded-[6px]">
      <div>
        <Text>Assignments</Text>
      </div>
      {isLoading && (
        <div>
          <LoadingWithText text="Loading assignments..." />
        </div>
      )}
      {!isLoading && (
        <div className="grid lg:grid-cols-3 md:grid-cols-2 grid-cols-1 gap-[10px]">
          {assignments.map((assignment) => {
            return (
              <AssignmentCard
                clickable={false}
                key={assignment.id}
                assignment={assignment}
              />
            );
          })}
        </div>
      )}
    </div>
  );
};

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
    <MainBox className="flex sm:flex-row flex-col gap-[10px]">
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
      <div className="dashboard grow p-[15px] rounded-[6px] bg-white">
        {user.role === "MENTOR" && <Students />}
        {user.role === "STUDENT" && <Assignments user={user} />}
      </div>
    </MainBox>
  );
};

export default Profile;
