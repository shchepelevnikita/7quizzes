CREATE TABLE IF NOT EXISTS user_roles_jsonb (
                                          userId varchar REFERENCES users_jsonb(id) ON DELETE CASCADE,
                                          "role" varchar,
                                          PRIMARY KEY (userId, "role")
);
