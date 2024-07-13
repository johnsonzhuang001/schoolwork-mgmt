import Text from "../component/Text";
import MainBox from "../component/MainBox";
import useAssignments from "../hook/assignment/useAssignments";
import useStudyGroup from "../hook/user/useStudyGroup";
import LoadingWithText from "../component/LoadingWithText";
import useSelf from "../hook/user/useSelf";
import AssignmentCard from "../component/AssignmentCard";
import UserCard from "../component/UserCard";

const Home = () => {
  const { self } = useSelf();
  const { assignments, isLoading: loadingAssignments } = useAssignments(
    self?.username ?? ""
  );
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
