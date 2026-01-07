ALTER TABLE places
    ADD COLUMN location_geog geography(Point,4326);

UPDATE places
SET location_geog = location::geography;

CREATE INDEX idx_places_location_geog
    ON places
    USING GIST(location_geog);

ALTER TABLE places
DROP COLUMN location;

ALTER TABLE places
    RENAME COLUMN location_geog TO location;

DROP INDEX IF EXISTS idx_places_location;
ALTER INDEX idx_places_location_geog RENAME TO idx_places_location;
