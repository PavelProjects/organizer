begin;

drop table _user, _user_credits, _comment, _notification, _task, _user_task, dict_notify_type, dict_task_status;
drop sequence main_id_sequence;
drop function getnextid;

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

create table _user_credits (
    id char(8) primary key default getnextid(),
    login varchar(32) not null unique,
    password varchar(128) not null,
    mail varchar(128) not null unique,
    active boolean not null default true,
    creation_date timestamp with time zone not null default now()
);

create table _user (
    id char(8) primary key default getnextid(),
    login varchar(32) references _user_credits(login) not null unique,
    name varchar(64) not null,
    avatar varchar(128)
);

create table _role(
    id char(8) primary key default getnextid(),
    name varchar(64) not null unique
);

create table _privilege(
    id char(8) primary key default getnextid(),
    name varchar(64) not null unique
);

create table _user_roles(
    id char(8) primary key default getnextid(),
    user_login varchar(32) references _user_credits(login) not null,
    role_id char(8) not null references _role(id),
    PRIMARY KEY (user_login, role_id)
);

create table _role_privileges(
    id char(8) primary key default getnextid(),
    role_id varchar(32) references _role(id) not null,
    privilege_id char(8) not null references _privilege(id)
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

create table _comment (
    id char(8) primary key default getnextid(),
    creation_date timestamp with time zone not null default now(),
    author_login varchar(32) not null references _user(login),
    task_id char(8) not null references _task(id),
    body text
);

create table _notification (
    id char(8) primary key default getnextid(),
    creation_date timestamp with time zone not null default now(),
    user_login varchar(32) references _user(login) not null,
    task_id char(8) references _task(id),
    comment_id char(8) references _comment(id),
    creator_login varchar(32) references _user(login),
    body text,
    type varchar(64) not null references dict_notify_type(name),
    checked boolean default false
);

create table _user_task (
    user_login varchar(32) not null references _user(login),
    task_id char(8) not null references _task(id)
);

insert into dict_notify_type (name, caption) values
    ('system', 'Система'),
    ('comment', 'Комментарий'),
    ('task', 'Задача');
insert into dict_task_status (name, caption) values ('new', 'New task');

insert into _privilege (name) values ('READ_ALL'), ('DELETE_ALL'), ('MODIFY_ALL');
insert into _role (name) values ('user'), ('moderator'), ('admin');

insert into _role_privileges (role_id, privilege_id) select r.id, p.id from _role r, _privilege p where r.name = 'user' and p.name = 'READ_ALL';
insert into _role_privileges (role_id, privilege_id) select r.id, p.id from _role r, _privilege p where r.name = 'moderator' and p.name in ('READ_ALL', 'MODIFY_ALL');
insert into _role_privileges (role_id, privilege_id) select r.id, p.id from _role r, _privilege p where r.name = 'admin' and p.name in ('READ_ALL', 'MODIFY_ALL', 'DELETE_ALL');

insert into _user_credits values (getnextid(), 'autotest_user', '$2a$10$8vzgsIktNcMSE1/QU49jVeO1dVo2sJFFdHncZbN.QAFEhXovqSJA6', 'jopa@mail.ru', true);
insert into _user (login, name) values ('autotest_user', 'main buddy');
insert into _user_roles (user_login, role_id) select 'autotest_user', r.id from _role r where r.name = 'admin';

insert into _user_credits values (getnextid(), 'basic_user', '$2a$10$8vzgsIktNcMSE1/QU49jVeO1dVo2sJFFdHncZbN.QAFEhXovqSJA6', 'jopa@mail.ru', true);
insert into _user (login, name) values ('basic_user', 'little boi');
insert into _user_roles (user_login, role_id) select 'basic_user', r.id from _role r where r.name = 'user';

commit;
