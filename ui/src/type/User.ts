export interface SelfDto {
  id: number;
  email: string;
  username: string;
  nickname: string | null;
  avatar: string | null;
  biography: string | null;
  role: string;
}

export enum UserRole {
  ADMIN = "ADMIN",
  MENTOR = "MENTOR",
  STUDENT = "STUDENT",
}

export interface UserDto {
  username: string;
  nickname: string;
  role: UserRole;
  biography: string | null;
}
