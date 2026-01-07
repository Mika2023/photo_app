CREATE INDEX idx_places_working_hours
ON places
USING GIN (working_hours);
