create table test
(
    id   int primary key,
    name varchar(255)
);

alter table board_comments
    add column test int;
alter table board_comments modify column content text default null;