-- Insert role data
-- INSERT INTO role (id,role) VALUES (1,'ROLE_USER');




-- Insert role data
INSERT INTO role (role) VALUES
('ROLE_USER'),
('ROLE_ADMIN'),
('ROLE_CREATE_ACTION'),
('ROLE_UPDATE_ACTION'),
('ROLE_DELETE_ACTION'),
('ROLE_CREATE_METRIC'),
('ROLE_UPDATE_METRIC'),
('ROLE_DELETE_METRIC'),
('ROLE_TRIGGER');


-- Insert user data
-- pass: 1234 
INSERT INTO user (id, deleted, email, password, userName) 
VALUES (500, false, 'admin@gmail.com', '$2a$10$ZAowrR3wiLLmKhkQd/eNWOSIaGY1wrK4NfkjWsRPGB0IlwVAW9QdO', 'admin2');

INSERT INTO user (id, deleted, email, password, userName)
VALUES (1001, false, 'yara@gmail.com', '$2a$10$ZAowrR3wiLLmKhkQd/eNWOSIaGY1wrK4NfkjWsRPGB0IlwVAW9QdO', 'Yara');

-- Retrieve the user ID for 'admin2'
SELECT id FROM user WHERE userName = 'admin2';

-- Retrieve all role IDs
SELECT id FROM role;

-- Insert user-role mappings for 'admin2'
INSERT INTO users_role (cust_id, role_id)
SELECT u.id AS cust_id, r.id AS role_id
FROM user u
CROSS JOIN role r
WHERE u.userName = 'admin2';
