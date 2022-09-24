INSERT INTO DEPARTMENT (`id`, `name`, `asset`) values ('1', '청년부', 0);
INSERT INTO DEPARTMENT (`id`, `name`, `asset`) values ('2', '중고등부', 0);
INSERT INTO DEPARTMENT (`id`, `name`, `asset`) values ('3', '유초등부', 0);
INSERT INTO DEPARTMENT (`id`, `name`, `asset`) values ('4', '유아부', 0);

INSERT INTO AUTHORITY (`authority_name`) values ('ROLE_ADMIN');
INSERT INTO AUTHORITY (`authority_name`) values ('ROLE_LEADER');
INSERT INTO AUTHORITY (`authority_name`) values ('ROLE_USER');

INSERT INTO MEMBER (`id`, `email`, `password`, `name`, `asset`, `department_id`) values ('1', 'admin', '$2a$10$qfKy2Md2U8ThUV0w/Om86uQBcvzqog3QAbCt0uhDpJ0olvPKXxn/C', '관리자', 0, '1');
INSERT INTO MEMBER (`id`, `email`, `password`, `name`, `asset`, `department_id`) values ('2', 'youth', '$2a$10$qfKy2Md2U8ThUV0w/Om86uQBcvzqog3QAbCt0uhDpJ0olvPKXxn/C', '청년부', 0, '1');
INSERT INTO MEMBER (`id`, `email`, `password`, `name`, `asset`, `department_id`) values ('3', 'secondary', '$2a$10$qfKy2Md2U8ThUV0w/Om86uQBcvzqog3QAbCt0uhDpJ0olvPKXxn/C', '중고등부', 0, '2');
INSERT INTO MEMBER (`id`, `email`, `password`, `name`, `asset`, `department_id`) values ('4', 'elementary', '$2a$10$qfKy2Md2U8ThUV0w/Om86uQBcvzqog3QAbCt0uhDpJ0olvPKXxn/C', '유초등부', 0, '3');
INSERT INTO MEMBER (`id`, `email`, `password`, `name`, `asset`, `department_id`) values ('5', 'child', '$2a$10$qfKy2Md2U8ThUV0w/Om86uQBcvzqog3QAbCt0uhDpJ0olvPKXxn/C', '유아부', 0, '4');

INSERT INTO MEMBER_AUTHORITY(`id`, `authority_name`) values ('1', 'ROLE_ADMIN');
INSERT INTO MEMBER_AUTHORITY(`id`, `authority_name`) values ('1', 'ROLE_LEADER');
INSERT INTO MEMBER_AUTHORITY(`id`, `authority_name`) values ('1', 'ROLE_USER');
INSERT INTO MEMBER_AUTHORITY(`id`, `authority_name`) values ('2', 'ROLE_LEADER');
INSERT INTO MEMBER_AUTHORITY(`id`, `authority_name`) values ('2', 'ROLE_USER');
INSERT INTO MEMBER_AUTHORITY(`id`, `authority_name`) values ('3', 'ROLE_LEADER');
INSERT INTO MEMBER_AUTHORITY(`id`, `authority_name`) values ('3', 'ROLE_USER');
INSERT INTO MEMBER_AUTHORITY(`id`, `authority_name`) values ('4', 'ROLE_LEADER');
INSERT INTO MEMBER_AUTHORITY(`id`, `authority_name`) values ('4', 'ROLE_USER');
INSERT INTO MEMBER_AUTHORITY(`id`, `authority_name`) values ('5', 'ROLE_LEADER');
INSERT INTO MEMBER_AUTHORITY(`id`, `authority_name`) values ('5', 'ROLE_USER');


