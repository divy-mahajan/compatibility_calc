-- Compatibility Calculator
-- Run this once in MySQL Workbench to set up your local database

DROP DATABASE IF EXISTS compatibility_db;
CREATE DATABASE compatibility_db;
USE compatibility_db;

-- Users table
CREATE TABLE users (
                       id               INT AUTO_INCREMENT PRIMARY KEY,
                       name             VARCHAR(50)  NOT NULL,
                       personality_code VARCHAR(4)   NOT NULL,
                       created_at       TIMESTAMP    DEFAULT NOW()
);

-- Traits table
CREATE TABLE traits (
                        id         INT AUTO_INCREMENT PRIMARY KEY,
                        user_id    INT          NOT NULL,
                        trait_name VARCHAR(40)  NOT NULL,
                        value      TINYINT      NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Results table
CREATE TABLE results (
                         id            INT AUTO_INCREMENT PRIMARY KEY,
                         user1_id      INT      NOT NULL,
                         user2_id      INT      NOT NULL,
                         score         TINYINT  NOT NULL,
                         calculated_at TIMESTAMP DEFAULT NOW(),
                         FOREIGN KEY (user1_id) REFERENCES users(id),
                         FOREIGN KEY (user2_id) REFERENCES users(id)
);

-- Question responses table
CREATE TABLE question_responses (
                                    id           INT AUTO_INCREMENT PRIMARY KEY,
                                    user_id      INT          NOT NULL,
                                    question_id  VARCHAR(20)  NOT NULL,
                                    option_value TINYINT      NOT NULL,
                                    FOREIGN KEY (user_id) REFERENCES users(id)
);