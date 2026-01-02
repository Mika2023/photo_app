CREATE TABLE place_to_tag (
   place_id BIGINT REFERENCES places(id) ON DELETE CASCADE,
   tag_id BIGINT REFERENCES tags(id) ON DELETE CASCADE,
   PRIMARY KEY(place_id, tag_id)
);