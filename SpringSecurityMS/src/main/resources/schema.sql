drop table if exists users_role;
drop table if exists role_seq;
drop table if exists users_seq;
drop table if exists role;
drop table if exists user;

-- Create the role table if it doesn't exist
CREATE TABLE role (
    id INT AUTO_INCREMENT PRIMARY KEY,
    role VARCHAR(255) NOT NULL
);


CREATE TABLE user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(255)NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    deleted TINYINT(1) DEFAULT 0
); 



CREATE TABLE users_role (
    cust_id BIGINT,
    role_id INT,
    PRIMARY KEY (cust_id, role_id),
    FOREIGN KEY (cust_id) REFERENCES user(id),
    FOREIGN KEY (role_id) REFERENCES role(id)
);
