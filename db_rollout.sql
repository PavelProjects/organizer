begin;

create table dict_notify_type (
    id serial primary key,
    name varchar(32) not null unique,
    caption varchar(128) not null
);
create unique index dict_notify_type_name on dict_notify_type (name);

create table dict_task_status (
    id serial primary key,
    name varchar(32) not null unique,
    caption varchar(128) not null
);
create unique index dict_task_status_name on dict_task_status (name);

create table _user (
    id serial primary key,
    login varchar(32) not null unique,
    password varchar(128) not null,
    name varchar(64) not null,
    avatar varchar(128)
);

create table _task (
    id serial primary key,
    name varchar(128) not null,
    description text,
    author varchar(32) references _user(login) not null,
    status varchar(64) references dict_task_status(name) not null,
    creation_date timestamp with time zone not null default now(),
    deadline timestamp with time zone
);

create table _notification (
    id serial primary key,
    creation_date timestamp with time zone not null default now(),
    user_login varchar(32) references _user(login) not null,
    task_id integer references _task(id),
    creator varchar(32) references _user(login),
    body text,
    type varchar(64) not null references dict_notify_type(name)
);

create table _comment (
    id serial primary key,
    creation_date timestamp with time zone not null default now(),
    author varchar(32) not null references _user(login),
    task_id integer not null references _task(id),
    body text
);

create table _user_task (
    user_login varchar(32) not null references _user(login),
    task_id integer not null references _task(id)
);

create table _user_role (
    id serial primary key,
    user_login varchar(32) not null references _user(login),
    role varchar(32) not null
);

insert into dict_task_status (name, caption) values ('new', 'New task');
insert into _user (login, password, name) values ('master', '1', 'main buddy');
insert into _user_role values ('master', 'admin');

commit;
