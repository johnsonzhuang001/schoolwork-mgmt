create sequence user_seq start with 1 increment by 1 no cache no cycle;
create table users (
   id bigint not null DEFAULT (NEXT VALUE FOR user_seq),
   username nvarchar(128) not null,
   password nvarchar(128) not null,
   nickname nvarchar(128),
   biography nvarchar(512),
   role nvarchar(32) not null,
   mentor_id bigint,
   peer_id bigint,
   created_at datetime2 not null,
   updated_at datetime2 not null,
   deleted_at datetime2 default NULL
)
alter table users add constraint user_pk PRIMARY KEY (id);

create sequence question_seq start with 1 increment by 1 no cache no cycle;
create table question (
   id bigint not null DEFAULT (NEXT VALUE FOR question_seq),
   assignment_id bigint not null,
   description nvarchar(1028) not null,
   option_a nvarchar(256) not null,
   option_b nvarchar(256) not null,
   option_c nvarchar(256) not null,
   option_d nvarchar(256) not null,
   answer char(1) not null,
   created_at datetime2 not null
)
alter table question add constraint question_pk PRIMARY KEY (id);

create sequence assignment_seq start with 1 increment by 1 no cache no cycle;
create table assignment (
  id bigint not null DEFAULT (NEXT VALUE FOR assignment_seq),
  title nvarchar(256) not null,
  level varchar(64) not null,
  deadline datetime2 not null,
  created_at datetime2 not null
)
alter table assignment add constraint assignment_pk PRIMARY KEY (id);

create sequence student_question_seq start with 1 increment by 1 no cache no cycle;
create table student_question (
    id bigint not null DEFAULT (NEXT VALUE FOR student_question_seq),
    student_id bigint not null,
    question_id bigint not null,
    answer char(1) not null,
    created_at datetime2 not null,
    updated_at datetime2 not null
)
alter table student_question add constraint student_question_pk PRIMARY KEY (id);

create sequence student_assignment_seq start with 1 increment by 1 no cache no cycle;
create table student_assignment (
  id bigint not null DEFAULT (NEXT VALUE FOR student_assignment_seq),
  student_id bigint not null,
  assignment_id bigint not null,
  score decimal, -- If null, it means the mentor hasn't given it a score
  created_at datetime2 not null,
  updated_at datetime2 not null
)
alter table student_assignment add constraint student_assignment_pk PRIMARY KEY (id);