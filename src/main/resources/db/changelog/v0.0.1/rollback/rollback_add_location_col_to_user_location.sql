ALTER TABLE user_location
ADD COLUMN latitude DECIMAL(9,6),
ADD COLUMN longitude DECIMAL(9,6);

UPDATE user_location
SET latitude = ST_Y(location::geometry),
    longitude = ST_X(location::geometry);

ALTER TABLE user_location
DROP COLUMN location;