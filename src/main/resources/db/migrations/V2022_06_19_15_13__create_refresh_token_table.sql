CREATE TABLE IF NOT EXISTS refresh_token (
    id varchar UNIQUE PRIMARY KEY NOT NULL,
    token varchar,
    expireDate varchar,
    userId varchar REFERENCES users_jsonb(id) ON DELETE CASCADE
);
