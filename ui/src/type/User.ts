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
