ALTER TABLE places
    ADD COLUMN location_geom geometry(Point,4326);

UPDATE places
SET location_geom = ST_SetSRID(location::geometry, 4326);

CREATE INDEX idx_places_location_geom
    ON places
    USING GIST(location_geom);

ALTER TABLE places
DROP COLUMN location;

ALTER TABLE places
    RENAME COLUMN location_geom TO location;

DROP INDEX IF EXISTS idx_places_location;
ALTER INDEX idx_places_location_geom RENAME TO idx_places_location;
