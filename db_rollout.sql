begin;

create sequence main_id_sequence;

create function getnextid() returns char(8) as
    $$ declare
      str text :=  '0123456789abcdefghijklmnopqrstuvwxyz';
      val bigint;
      id_ text;
      mod int;
      begin
      val:=nextval('main_id_sequence');
      id_:='';
      while (length(id_) < 8) loop
        mod = val % 36;
        id_:=substring(str,mod+1,1)||id_;
        val = val / 36;
      end loop;
      return id_;
      return 'null';
      end;   $$
language plpgsql;

create table dict_notify_type (
    id char(8) primary key default getnextid(),
    name varchar(32) not null unique,
    caption varchar(128) not null
);
create unique index dict_notify_type_name on dict_notify_type (name);

create table dict_task_status (
    id char(8) primary key default getnextid(),
    name varchar(32) not null unique,
    caption varchar(128) not null
);
create unique index dict_task_status_name on dict_task_status (name);

create table _user (
    id char(8) primary key default getnextid(),
    login varchar(32) not null unique,
    password varchar(128) not null,
    name varchar(64) not null,
    avatar varchar(128)
);

create table _task (
    id char(8) primary key default getnextid(),
    name varchar(128) not null,
    description text,
    author varchar(32) references _user(login) not null,
    status varchar(64) references dict_task_status(name) not null,
    creation_date timestamp with time zone not null default now(),
    deadline timestamp with time zone
);

create table _notification (
    id char(8) primary key default getnextid(),
    creation_date timestamp with time zone not null default now(),
    user_login varchar(32) references _user(login) not null,
    task_id char(8) references _task(id),
    creator varchar(32) references _user(login),
    body text,
    type varchar(64) not null references dict_notify_type(name)
);

create table _comment (
    id char(8) primary key default getnextid(),
    creation_date timestamp with time zone not null default now(),
    author varchar(32) not null references _user(login),
    task_id char(8) not null references _task(id),
    body text
);

create table _user_task (
    user_login varchar(32) not null references _user(login),
    task_id char(8) not null references _task(id)
);

insert into dict_task_status (name, caption) values ('new', 'New task');
insert into _user (login, password, name) values ('autotest_user', '1', 'main buddy');

commit;
