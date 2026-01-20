CREATE TABLE route_places (
   route_id BIGINT REFERENCES routes(id) ON DELETE CASCADE,
   place_id BIGINT REFERENCES places(id) ON DELETE CASCADE,
   position INT NOT NULL,
   PRIMARY KEY(route_id, place_id)
);

ALTER TABLE routes DROP CONSTRAINT IF EXISTS routes_to_place_id_fkey;
ALTER TABLE routes DROP CONSTRAINT IF EXISTS routes_from_place_id_fkey;

ALTER TABLE routes
DROP COLUMN IF EXISTS to_place_id,
    DROP COLUMN IF EXISTS from_place_id,
    DROP COLUMN IF EXISTS from_location,
    DROP COLUMN IF EXISTS distance_meters,
    DROP COLUMN IF EXISTS estimated_time_minutes;