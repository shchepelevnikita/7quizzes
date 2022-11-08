CREATE TABLE IF NOT EXISTS rooms_jsonb (
                                     id varchar UNIQUE PRIMARY KEY NOT NULL,
                                     roomData jsonb
);
