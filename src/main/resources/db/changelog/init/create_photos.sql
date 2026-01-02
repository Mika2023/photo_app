CREATE TABLE photos (
   id BIGSERIAL PRIMARY KEY,
   place_id BIGINT REFERENCES places(id) ON DELETE SET NULL,
   user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
   image_url TEXT NOT NULL,
   created_at TIMESTAMP DEFAULT now()
);