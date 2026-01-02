CREATE TABLE route_places (
   route_id BIGINT REFERENCES routes(id) ON DELETE CASCADE,
   place_id BIGINT REFERENCES places(id) ON DELETE CASCADE,
   position INT NOT NULL,
   PRIMARY KEY(route_id, place_id)
);
