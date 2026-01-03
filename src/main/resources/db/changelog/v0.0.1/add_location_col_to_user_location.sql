ALTER TABLE user_location
ADD COLUMN location GEOGRAPHY(Point,4326);

UPDATE user_location
SET location = ST_SetSRID(
        ST_MakePoint(longitude::double precision, latitude::double precision),
        4326
        );

ALTER TABLE user_location
DROP COLUMN latitude,
DROP COLUMN longitude;

ALTER TABLE user_location
ALTER COLUMN location SET NOT NULL;