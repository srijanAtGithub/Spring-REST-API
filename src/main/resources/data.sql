INSERT INTO user_details(id, birth_date, name)
VALUES (10001, current_date(), 'Srijan');

INSERT INTO user_details(id, birth_date, name)
VALUES (10002, current_date(), 'ABCD');

INSERT INTO user_details(id, birth_date, name)
VALUES (10003, current_date(), 'XYZ');

INSERT INTO post(id, description, user_id)
VALUES (20001, 'I want to Learn AWS', 10001);

INSERT INTO post(id, description, user_id)
VALUES (20002, 'I want to Learn DevOps', 10001);

INSERT INTO post(id, description, user_id)
VALUES (20003, 'I want to Learn Get AWS Certified', 10002);

INSERT INTO post(id, description, user_id)
VALUES (20004, 'I want to Learn Multi Cloud', 10002);