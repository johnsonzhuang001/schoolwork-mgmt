create sequence user_seq start with 1 increment by 1 no cache no cycle;
create table users (
   id bigint not null DEFAULT (NEXT VALUE FOR user_seq),
   username nvarchar(128) not null,
   password nvarchar(128) not null,
   nickname nvarchar(128),
   role nvarchar(32) not null,
   created_at datetime2 not null,
   updated_at datetime2 not null,
   deleted_at datetime2 default NULL
)
alter table users add constraint user_pk PRIMARY KEY (id);