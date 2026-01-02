CREATE TABLE place_to_category (
   place_id BIGINT REFERENCES places(id) ON DELETE CASCADE,
   category_id BIGINT REFERENCES categories(id) ON DELETE CASCADE,
   PRIMARY KEY(place_id, category_id)
);