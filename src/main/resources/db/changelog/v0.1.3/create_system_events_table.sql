CREATE TABLE system_events (
    id BIGSERIAL PRIMARY KEY,
    event_name TEXT NOT NULL,
    create_date TIMESTAMP DEFAULT now(),
    user_id BIGINT REFERENCES users(id) ON DELETE SET NULL
);