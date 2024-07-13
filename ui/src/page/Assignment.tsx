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
import useSubmitAssignment from "../hook/assignment/useSubmitAssignment";
import useWithdrawAssignment from "../hook/assignment/useWithdrawAssignment";

const CheckBox = ({
  text,
  selected,
  onSelect,
  disabled = false,
}: {
  text: string;
  selected: boolean;
  onSelect: () => void;
  disabled?: boolean;
}) => {
  return (
    <div
      className={`w-fit flex items-center gap-[5px] ${
        disabled ? "" : "cursor-pointer"
      }`}
      onClick={() => {
        if (!disabled) onSelect();
      }}
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
  error,
  disabled,
}: {
  answer: QuestionAnswer | null;
  index: number;
  question: QuestionDto;
  onSelect: (answer: QuestionAnswer | null) => void;
  error: string;
  disabled: boolean;
}) => {
  return (
    <div className="question flex flex-col gap-[10px] p-[10px] border-[1px] border-whitegray rounded-[6px]">
      <div className="flex flex-col gap-[5px]">
        <Text size="sm">
          {index}. {question.description}
        </Text>
        {error && (
          <Text type="danger" size="sm">
            {error}
          </Text>
        )}
      </div>
      <div className="options flex flex-col gap-[5px]">
        <CheckBox
          text={question.optionA}
          disabled={disabled}
          selected={answer === QuestionAnswer.A}
          onSelect={() =>
            onSelect(answer === QuestionAnswer.A ? null : QuestionAnswer.A)
          }
        />
        <CheckBox
          text={question.optionB}
          disabled={disabled}
          selected={answer === QuestionAnswer.B}
          onSelect={() =>
            onSelect(answer === QuestionAnswer.B ? null : QuestionAnswer.B)
          }
        />
        <CheckBox
          text={question.optionC}
          disabled={disabled}
          selected={answer === QuestionAnswer.C}
          onSelect={() =>
            onSelect(answer === QuestionAnswer.C ? null : QuestionAnswer.C)
          }
        />
        <CheckBox
          text={question.optionD}
          disabled={disabled}
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
  const { submitting, submitAssignment } = useSubmitAssignment();
  const { withdrawing, withdrawSubmission } = useWithdrawAssignment();
  const [errors, setErrors] = useState<Array<string>>([]);

  useEffect(() => {
    if (assignment) {
      setAnswers(assignment.questions.map((question) => question.answer));
      setErrors(assignment.questions.map((_) => ""));
    }
  }, [assignment]);

  const onSave = () => {
    if (assignment) {
      submitAssignment({
        readyForScore: false,
        request: {
          id: assignmentId,
          questions: assignment.questions.map((question, index) => {
            return {
              id: question.id,
              answer: answers[index],
            };
          }),
        },
      });
    }
  };

  const hasPassedDeadline = () => {
    return (
      !!assignment && DateTime.now() > DateTime.fromISO(assignment.deadline)
    );
  };

  const hasScore = () => {
    return assignment && assignment?.score !== null;
  };

  const validateAssignment = () => {
    let valid = true;
    if (assignment) {
      setErrors(() => assignment.questions.map((_) => ""));
      const newErrors = [...errors];
      assignment.questions.forEach((_, index) => {
        if (!answers[index]) {
          newErrors[index] = "Please answer this question.";
          valid = false;
        }
      });
      setErrors(() => newErrors);
    }
    return valid;
  };

  const onSubmit = () => {
    if (assignment && validateAssignment()) {
      submitAssignment({
        readyForScore: true,
        request: {
          id: assignmentId,
          questions: assignment.questions.map((question, index) => {
            return {
              id: question.id,
              answer: answers[index],
            };
          }),
        },
      });
    }
  };

  if (!assignment || isLoading) return <Loading />;

  return (
    <MainBox>
      <div className="flex flex-col gap-[15px] p-[10px] bg-white rounded-[6px]">
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
            {assignment.submitted && (
              <>
                {!hasScore() && !hasPassedDeadline() && (
                  <Button
                    type="outline"
                    color="red"
                    loading={withdrawing}
                    onClick={() => withdrawSubmission(assignment.id)}
                    text="Withdraw"
                  />
                )}
                <Button
                  type="outline"
                  color="green"
                  disabled
                  onClick={() => {}}
                  text={
                    hasScore() ? `Your Score: ${assignment.score}` : "Submitted"
                  }
                />
              </>
            )}
            {!assignment.submitted && (
              <>
                {hasPassedDeadline() && (
                  <Button
                    type="outline"
                    color="secondary"
                    disabled
                    onClick={() => {}}
                    text="Deadline has passed"
                  />
                )}
                {!hasPassedDeadline() && (
                  <>
                    <Button
                      type="outline"
                      text="Save"
                      loading={submitting}
                      onClick={onSave}
                    />
                    <Button
                      type="outline"
                      color="blue"
                      text="Submit"
                      loading={submitting}
                      onClick={onSubmit}
                    />
                  </>
                )}
              </>
            )}
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
                error={errors[index]}
                onSelect={(answer) =>
                  setAnswers((prev) => {
                    const newAnswers = [...prev];
                    newAnswers[index] = answer;
                    return newAnswers;
                  })
                }
                disabled={assignment.submitted || hasPassedDeadline()}
              />
            );
          })}
        </div>
      </div>
    </MainBox>
  );
};

export default Assignment;
