CREATE TABLE IF NOT EXISTS user_roles (
    userId varchar REFERENCES users(id) ON DELETE CASCADE,
    "role" varchar,
    PRIMARY KEY (userId, "role")
);
