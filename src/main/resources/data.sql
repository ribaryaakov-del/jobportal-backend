INSERT INTO users ( name, email, password, provider)
VALUES( 'admin', 'admin@example.com',
        '$2y$10$wQuA5eMZWky7IEaBOZUWSuc4Pe7HxCQz9EyFU21A9EHqhOTRLA5C2', 'LOCAL');

INSERT INTO users ( name, email, password, provider)
VALUES ( 'user', 'user@example.com',
        '$2y$10$of8X8GCzsinvCrWpQArJjeYNHOH7irRCWUn2t4B2ti4uXsEZYFcj6', 'LOCAL');

INSERT INTO jobs( title, description, company, posted_date)
VALUES ('Software Tester', 'Test the software', 'Tech Corp', CURRENT_TIMESTAMP);

INSERT INTO jobs(title, description, company, posted_date)
VALUES ( 'Backend Developer', 'Develop backend services using Java', 'Innovate Ltd.', CURRENT_TIMESTAMP);

INSERT INTO jobs(title, description, company, posted_date)
VALUES ( 'Security Analyst', 'Ensuring security of web applications', 'CyberX', CURRENT_TIMESTAMP);