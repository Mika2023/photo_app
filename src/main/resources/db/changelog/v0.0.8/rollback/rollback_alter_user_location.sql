ALTER TABLE user_location
    ADD COLUMN location_geog geography(Point,4326);

UPDATE user_location
SET location_geog = location::geography;

ALTER TABLE user_location
DROP COLUMN location;

ALTER TABLE user_location
    RENAME COLUMN location_geog TO location;

ALTER TABLE user_location
    ALTER COLUMN location SET NOT NULL;