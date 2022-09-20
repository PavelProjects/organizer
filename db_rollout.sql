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

create table users (
    id serial primary key,
    login varchar(32) not null unique,
    password varchar(128) not null,
    name varchar(64) not null,
    avatar varchar(128)
);

create table tasks (
    id serial primary key,
    name varchar(128) not null,
    description text,
    author varchar(32) references users(login),
    status varchar(64) references dict_task_status(name),
    creation_date timestamp with time zone not null default now(),
    deadline timestamp with time zone
);

create table notifications (
    id serial primary key,
    creation_date timestamp with time zone not null default now(),
    user_login varchar(32) references users(login) not null,
    task_id integer references tasks(id),
    creator varchar(32) references users(login),
    body text,
    type varchar(64) not null references dict_notify_type(name)
);

create table comments (
    id serial primary key,
    creation_date timestamp with time zone not null default now(),
    author varchar(32) not null references users(login),
    task_id integer not null references tasks(id),
    body text
);

create table user_tasks (
    user_login varchar(32) not null references users(login),
    task_id integer not null references tasks(id)
);

commit;
