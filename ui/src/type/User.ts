export interface SelfDto {
  id: number;
  email: string;
  username: string;
  nickname: string | null;
  avatar: string | null;
  biography: string | null;
  role: string;
}

export interface UserDto {
  id: number;
  username: string;
  nickname: string | null;
  avatar: string | null;
  biography: string | null;
  followed: boolean;
}

export interface UserProfileDto {
  user: UserDto;
  followerCount: number;
  followingCount: number;
}
