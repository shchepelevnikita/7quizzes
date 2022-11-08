CREATE TABLE IF NOT EXISTS users_jsonb (
                                    id varchar UNIQUE PRIMARY KEY NOT NULL,
                                    userData jsonb
);