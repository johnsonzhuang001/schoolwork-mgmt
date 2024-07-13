import Text from "../component/Text";
import MainBox from "../component/MainBox";
import useAssignments from "../hook/assignment/useAssignments";
import { AssignmentDto } from "../type/Assignment";
import { DateTime } from "luxon";
import { useNavigate } from "react-router-dom";
import useStudyGroup from "../hook/user/useStudyGroup";
import LoadingWithText from "../component/LoadingWithText";
import { UserDto, UserRole } from "../type/User";
import { AiOutlineMessage } from "react-icons/ai";
import { MdOutlineEmail } from "react-icons/md";
import useSelf from "../hook/user/useSelf";
import { Tooltip } from "@mui/material";

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
  return (
    <div className="user-card relative rounded-[6px] bg-white  overflow-hidden">
      <div className="absolute w-full top-0 h-[64px] bg-blue z-0" />
      <div className="relative h-full flex flex-col items-center gap-[10px] pt-[30px] pb-[20px] px-[10px] z-1">
        <div className="w-[60px] h-[60px] rounded-[30px] bg-whitegray" />
        <div className="flex flex-col items-center">
          <Text>{user.nickname}</Text>
          <Text type="secondary" size="xs">
            {toReadableRole(user.role)}
          </Text>
        </div>
        <Text size="sm" className="text-center grow">
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

const AssignmentCard = ({ assignment }: { assignment: AssignmentDto }) => {
  const navigate = useNavigate();
  return (
    <div
      className="flex flex-col gap-[10px] p-[15px] rounded-[20px] border-[1px] border-whitegray cursor-pointer hover:shadow-card transition-shadow duration-300"
      onClick={() => navigate(`/assignment/${assignment.id}`)}
    >
      <div className="w-full h-[125px] rounded-[15px] bg-whitegray" />
      <div className="flex flex-col gap-[5px]">
        <Text size="lg">{assignment.title}</Text>
        <div className="flex gap-[15px]">
          <Text size="sm" type="secondary">
            {assignment.level}
          </Text>
          <Text size="sm" type="secondary">
            DDL:{" "}
            {DateTime.fromISO(assignment.deadline).toFormat(
              "MMM dd yyyy HH:mm"
            )}
          </Text>
        </div>
      </div>
      <div className="flex flex-col gap-[5px]">
        {assignment.submitted ? (
          <Text type="success" size="sm">
            Submitted
          </Text>
        ) : (
          <Text size="sm">Progress</Text>
        )}
        <div className="w-full h-[10px] rounded-[5px] bg-whitegray overflow-hidden">
          <div
            className={`h-[10px] rounded-[5px] ${
              assignment.submitted ? "bg-green" : "bg-blue"
            }`}
            style={{
              width: `${
                (assignment.finishCount / assignment.questionCount) * 100
              }%`,
            }}
          />
        </div>
      </div>
      <div className="flex gap-[5px]">
        <div className="flex-1 flex flex-col gap-[10px] p-[10px] rounded-[6px] border-whitegray border-[1px]">
          <Text size="sm">Score</Text>
          <div className="w-full h-[60px] flex justify-center items-center">
            <Text size="xl">N/A</Text>
          </div>
        </div>
        <div className="flex-1 flex flex-col gap-[10px] p-[10px] rounded-[6px] border-whitegray border-[1px]">
          <Text size="sm">Grade</Text>
          <div className="w-full h-[60px] flex justify-center items-center">
            <Text size="xl">N/A</Text>
          </div>
        </div>
      </div>
    </div>
  );
};

const Home = () => {
  const { assignments, isLoading: loadingAssignments } = useAssignments();
  const { studyGroup, isLoading: loadingStudyGroup } = useStudyGroup();
  return (
    <MainBox className="flex flex-col gap-[15px]">
      <div className="w-full flex flex-col gap-[10px]">
        <div>
          <Text>Study Group</Text>
        </div>
        {loadingStudyGroup && (
          <div>
            <LoadingWithText text="Loading your study group..." />
          </div>
        )}
        {!loadingAssignments && (
          <div className="grid lg:grid-cols-5 md:grid-cols-4 sm:grid-cols-3 grid-cols-2 gap-[10px]">
            {studyGroup.map((user) => (
              <UserCard key={user.username} user={user} />
            ))}
          </div>
        )}
      </div>
      <div className="w-full flex flex-col p-[10px] gap-[10px] bg-white rounded-[6px]">
        <div>
          <Text>Assignments</Text>
        </div>
        {loadingAssignments && (
          <div>
            <LoadingWithText text="Loading assignments..." />
          </div>
        )}
        {!loadingAssignments && (
          <div className="grid lg:grid-cols-4 md:grid-cols-3 sm:grid-cols-2 grid-cols-1 gap-[10px]">
            {assignments.map((assignment) => {
              return (
                <AssignmentCard key={assignment.id} assignment={assignment} />
              );
            })}
          </div>
        )}
      </div>
    </MainBox>
  );
};

export default Home;
