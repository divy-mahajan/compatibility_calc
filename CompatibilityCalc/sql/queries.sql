USE compatibility_db;

-- View all users
SELECT * FROM users;

-- View all traits for a specific user
SELECT * FROM traits WHERE user_id = 1;

-- View all results with names instead of IDs
SELECT
    u1.name        AS person_1,
    u2.name        AS person_2,
    r.score        AS compatibility_score,
    r.calculated_at
FROM results r
         JOIN users u1 ON r.user1_id = u1.id
         JOIN users u2 ON r.user2_id = u2.id
ORDER BY r.score DESC;

-- Top 5 most compatible pairs
SELECT
    u1.name  AS person_1,
    u2.name  AS person_2,
    r.score  AS score
FROM results r
         JOIN users u1 ON r.user1_id = u1.id
         JOIN users u2 ON r.user2_id = u2.id
ORDER BY r.score DESC
    LIMIT 5;

-- View all responses for a specific user
SELECT
    question_id,
    option_value
FROM question_responses
WHERE user_id = 1;

-- View full profile of a user (traits + personality code)
SELECT
    u.name,
    u.personality_code,
    t.trait_name,
    t.value
FROM users u
         JOIN traits t ON u.id = t.user_id
WHERE u.id = 1;

-- Count total sessions run
SELECT COUNT(*) AS total_sessions FROM results;

-- Delete all data but keep tables (useful for resetting during testing)
SET FOREIGN_KEY_CHECKS = 0;
TRUNCATE TABLE question_responses;
TRUNCATE TABLE traits;
TRUNCATE TABLE results;
TRUNCATE TABLE users;
SET FOREIGN_KEY_CHECKS = 1;