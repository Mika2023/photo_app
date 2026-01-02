CREATE TABLE user_location (
   user_id    BIGINT PRIMARY KEY REFERENCES users (id) ON DELETE CASCADE,
   latitude   DECIMAL(9, 6),
   longitude  DECIMAL(9, 6),
   updated_at TIMESTAMP
);