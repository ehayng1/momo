DROP TABLE IF EXISTS user;

create table user
(
    id       BigInt       not null auto_increment,
    login_id varchar(100) not null,
    password varchar(100),
    status   int,
    regdate  timestamp default now(),
    primary key (id)
);
INSERT INTO user(login_id, status)
VALUES ('test', '1');
INSERT INTO user(login_id, status)
VALUES ('dk', '2');
INSERT INTO user(login_id, status)
VALUES ('zzzz', '3');
