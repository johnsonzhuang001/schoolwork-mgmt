export enum UserRole {
  ADMIN = "ADMIN",
  MENTOR = "MENTOR",
  STUDENT = "STUDENT",
}

export interface UserDto {
  username: string;
  nickname: string;
  role: UserRole;
  biography: string;
}

export interface ProgressDto {
  score: number;
  peerScoreOverridden: boolean;
  mentorPasswordOverridden: boolean;
  peerAllScoresOverridden: boolean;
  startedAt: string;
}
