ALTER TABLE user_location
    ADD COLUMN location_geom geometry(Point,4326);

UPDATE user_location
SET location_geom = ST_SetSRID(location::geometry, 4326);

ALTER TABLE user_location
DROP COLUMN location;

ALTER TABLE user_location
    RENAME COLUMN location_geom TO location;

ALTER TABLE user_location
    ALTER COLUMN location SET NOT NULL;
