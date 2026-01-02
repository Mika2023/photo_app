CREATE TABLE routes (
   id BIGSERIAL PRIMARY KEY,
   user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
   name TEXT,
   description TEXT,
   path GEOGRAPHY(LineString,4326),
   created_at TIMESTAMP DEFAULT now()
);