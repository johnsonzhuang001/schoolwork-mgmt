import { useNavigate, useParams } from "react-router-dom";
import useAssignment from "../hook/assignment/useAssignment";
import MainBox from "../component/MainBox";
import React, { useEffect, useState } from "react";
import { IoArrowBackOutline } from "react-icons/io5";
import Text from "../component/Text";
import Button from "../component/Button";
import Loading from "../component/Loading";
import { DateTime } from "luxon";
import { QuestionAnswer, QuestionDto } from "../type/Assignment";
import { useSaveAssignment } from "../hook/assignment/useSubmitAssignment";

const CheckBox = ({
  text,
  selected,
  onSelect,
}: {
  text: string;
  selected: boolean;
  onSelect: () => void;
}) => {
  return (
    <div
      className="w-fit flex items-center gap-[5px] cursor-pointer"
      onClick={onSelect}
    >
      <div className="w-[18px] h-[18px] flex justify-center items-center rounded-[4px] border-[1px] border-secondary">
        <div
          className={`w-[12px] h-[12px] rounded-[3px] bg-primary ${
            selected ? "" : "opacity-0"
          } transition-opacity duration-300`}
        />
      </div>
      <Text size="sm">{text}</Text>
    </div>
  );
};

const QuesitonCard = ({
  index,
  question,
  onSelect,
  answer,
}: {
  answer: QuestionAnswer | null;
  index: number;
  question: QuestionDto;
  onSelect: (answer: QuestionAnswer | null) => void;
}) => {
  return (
    <div className="question flex flex-col gap-[10px] p-[10px] border-[1px] border-whitegray rounded-[6px]">
      <div>
        <Text size="sm">
          {index}. {question.description}
        </Text>
      </div>
      <div className="options flex flex-col gap-[5px]">
        <CheckBox
          text={question.optionA}
          selected={answer === QuestionAnswer.A}
          onSelect={() =>
            onSelect(answer === QuestionAnswer.A ? null : QuestionAnswer.A)
          }
        />
        <CheckBox
          text={question.optionB}
          selected={answer === QuestionAnswer.B}
          onSelect={() =>
            onSelect(answer === QuestionAnswer.B ? null : QuestionAnswer.B)
          }
        />
        <CheckBox
          text={question.optionC}
          selected={answer === QuestionAnswer.C}
          onSelect={() =>
            onSelect(answer === QuestionAnswer.C ? null : QuestionAnswer.C)
          }
        />
        <CheckBox
          text={question.optionD}
          selected={answer === QuestionAnswer.D}
          onSelect={() =>
            onSelect(answer === QuestionAnswer.D ? null : QuestionAnswer.D)
          }
        />
      </div>
    </div>
  );
};

const Assignment = () => {
  const { assignmentId: assignmentIdStr } = useParams();
  const navigate = useNavigate();
  const assignmentId = assignmentIdStr ? Number.parseInt(assignmentIdStr) : 0;
  const { assignment, isLoading } = useAssignment(assignmentId);
  const [answers, setAnswers] = useState<Array<QuestionAnswer | null>>([]);
  const { saving, saveAssignment } = useSaveAssignment();

  useEffect(() => {
    if (assignment) {
      setAnswers(assignment.questions.map((question) => question.answer));
    }
  }, [assignment]);

  const onSave = () => {
    if (assignment) {
      saveAssignment({
        id: assignmentId,
        questions: assignment.questions.map((question, index) => {
          return {
            id: question.id,
            answer: answers[index],
          };
        }),
      });
    }
  };

  if (!assignment || isLoading) return <Loading />;

  return (
    <MainBox>
      <div className="flex flex-col gap-[15px] p-[10px] bg-white">
        <div className="actions w-full flex justify-between items-center">
          <div
            className="p-[5px] rounded-[6px] border-[1px] border-secondary cursor-pointer opacity-90 hover:opacity-70 transition-opacity duration-300"
            onClick={() => navigate("/")}
          >
            <Text type="secondary">
              <IoArrowBackOutline size="20px" />
            </Text>
          </div>
          <div className="buttons flex gap-[10px]">
            <Button
              type="outline"
              text="Save"
              loading={saving}
              onClick={onSave}
            />
            <Button
              type="outline"
              color="blue"
              text="Submit"
              onClick={() => {}}
            />
          </div>
        </div>
        <div className="title flex flex-col gap-[3px]">
          <Text size="xl">{assignment.title}</Text>
          <div className="flex gap-[10px]">
            <Text type="secondary" size="sm">
              Difficulty: {assignment.level}
            </Text>
            <Text type="secondary" size="sm">
              Deadline:{" "}
              {DateTime.fromISO(assignment.deadline).toFormat(
                "MMM dd yyyy HH:mm"
              )}
            </Text>
          </div>
        </div>
        <div className="questions flex flex-col gap-[10px]">
          {assignment.questions.map((question, index) => {
            return (
              <QuesitonCard
                key={question.id}
                answer={answers[index]}
                index={index + 1}
                question={question}
                onSelect={(answer) =>
                  setAnswers((prev) => {
                    const newAnswers = [...prev];
                    newAnswers[index] = answer;
                    return newAnswers;
                  })
                }
              />
            );
          })}
        </div>
      </div>
    </MainBox>
  );
};

export default Assignment;
