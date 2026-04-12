-- Resets all data without dropping the database or tables
-- Run this in MySQL Workbench when you want a clean slate during testing

USE compatibility_db;

SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE question_responses;
TRUNCATE TABLE traits;
TRUNCATE TABLE results;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;

SELECT 'Database reset complete.' AS status;