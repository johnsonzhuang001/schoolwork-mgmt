import { AssignmentDto } from "../type/Assignment";
import { useNavigate } from "react-router-dom";
import { TextColor } from "../type/Color";
import Text from "./Text";
import { DateTime } from "luxon";

const AssignmentCard = ({
  assignment,
  clickable = true,
}: {
  assignment: AssignmentDto;
  clickable?: boolean;
}) => {
  const navigate = useNavigate();
  const getScoreTextType = (score: number | null): TextColor => {
    if (score === null) {
      return "primary";
    }
    if (score >= 90) {
      return "success";
    }
    if (score >= 80) {
      return "link";
    }
    if (score >= 60) {
      return "warning";
    }
    return "danger";
  };

  return (
    <div
      className={`flex flex-col gap-[10px] p-[15px] rounded-[20px] border-[1px] border-whitegray ${
        clickable
          ? "cursor-pointer hover:shadow-card transition-shadow duration-300"
          : ""
      }`}
      onClick={() => {
        if (clickable) navigate(`/assignment/${assignment.id}`);
      }}
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
            {assignment.score !== null ? "Scored" : "Submitted"}
          </Text>
        ) : (
          <Text size="sm">In Progress</Text>
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
          <Text size="sm" type="secondary">
            Score
          </Text>
          <div className="w-full h-[60px] flex justify-center items-center">
            <Text size="2xl" type={getScoreTextType(assignment.score)}>
              {assignment.score !== null ? assignment.score : "N/A"}
            </Text>
          </div>
        </div>
        <div className="flex-1 flex flex-col gap-[10px] p-[10px] rounded-[6px] border-whitegray border-[1px]">
          <Text size="sm" type="secondary">
            Grade
          </Text>
          <div className="w-full h-[60px] flex justify-center items-center">
            <Text size="2xl" type={getScoreTextType(assignment.score)}>
              {assignment.grade !== null ? assignment.grade : "N/A"}
            </Text>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AssignmentCard;
