CREATE TABLE places (
   id BIGSERIAL PRIMARY KEY,
   name TEXT NOT NULL,
   description TEXT,
   location GEOGRAPHY(Point,4326),
   location_description JSONB,
   working_hours JSONB,
   visit_cost NUMERIC(10,2),
   created_at TIMESTAMP DEFAULT now()
);