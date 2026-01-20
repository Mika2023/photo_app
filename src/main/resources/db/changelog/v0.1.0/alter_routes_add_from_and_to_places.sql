ALTER TABLE routes
ADD COLUMN to_place_id BIGINT REFERENCES places(id) ON DELETE CASCADE,
ADD COLUMN from_place_id BIGINT REFERENCES places(id) ON DELETE CASCADE,
ADD COLUMN from_location geometry(Point,4326),
ADD COLUMN distance_meters INTEGER,
ADD COLUMN estimated_time_minutes INTEGER;

DROP TABLE IF EXISTS route_places;