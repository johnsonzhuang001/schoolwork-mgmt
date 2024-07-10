export interface AssignmentDto {
  id: number;
  title: string;
  level: string;
  deadline: string;
  questionCount: number;
  finishCount: number;
}

export interface QuestionDto {
  id: number;
  description: string;
  optionA: string;
  optionB: string;
  optionC: string;
  optionD: string;
}

export interface AssignmentWithQuestionsDto {
  id: number;
  title: string;
  questions: ReadonlyArray<QuestionDto>;
}
