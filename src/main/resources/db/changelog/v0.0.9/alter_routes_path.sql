ALTER TABLE routes ADD COLUMN path_geom geometry(LineString, 4326);

UPDATE routes
SET path_geom = ST_Force2D(path::geometry);

ALTER TABLE routes
DROP COLUMN path;

ALTER TABLE routes
    RENAME COLUMN path_geom TO path;