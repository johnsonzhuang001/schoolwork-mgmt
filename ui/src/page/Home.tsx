import Text from "../component/Text";
import MainBox from "../component/MainBox";
import useAssignments from "../hook/assignment/useAssignments";
import Loading from "../component/Loading";
import { AssignmentDto } from "../type/Assignment";
import { DateTime } from "luxon";

const AssignmentCard = ({ assignment }: { assignment: AssignmentDto }) => {
  return (
    <div className="flex flex-col gap-[10px] p-[15px] rounded-[20px] border-[1px] border-whitegray cursor-pointer hover:shadow-card transition-shadow duration-300">
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
        <Text size="sm">Progress</Text>
        <div className="w-full h-[10px] rounded-[5px] bg-whitegray overflow-hidden">
          <div
            className="h-[10px] rounded-[5px] bg-blue"
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
  const { assignments, isLoading } = useAssignments();
  return (
    <MainBox>
      <div className="w-full flex flex-col p-[10px] gap-[10px] bg-white rounded-[6px]">
        <div>
          <Text>Assignments</Text>
        </div>
        {isLoading && <Loading />}
        {!isLoading && (
          <div className="grid lg:grid-cols-4 md:grid-cols-3 sm:grid-cols-2 grid-cols-1">
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
