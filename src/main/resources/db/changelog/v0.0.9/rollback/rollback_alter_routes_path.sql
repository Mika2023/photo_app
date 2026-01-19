ALTER TABLE routes ADD COLUMN path_geo geography(LineString, 4326);

UPDATE routes
SET path_geo = path::geography;

ALTER TABLE routes
DROP COLUMN path;

ALTER TABLE routes
    RENAME COLUMN path_geo TO path;