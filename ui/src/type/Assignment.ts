export interface AssignmentDto {
  id: number;
  title: string;
  level: string;
  deadline: string;
  submitted: boolean;
  score: number | null;
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
  answer: QuestionAnswer | null;
}

export interface AssignmentWithQuestionsDto {
  id: number;
  title: string;
  level: string;
  deadline: string;
  submitted: boolean;
  score: number | null;
  questions: ReadonlyArray<QuestionDto>;
}

export enum QuestionAnswer {
  A = "A",
  B = "B",
  C = "C",
  D = "D",
}
