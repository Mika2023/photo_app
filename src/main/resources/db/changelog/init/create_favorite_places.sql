CREATE TABLE favorite_places (
    user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
    place_id BIGINT REFERENCES places(id) ON DELETE CASCADE,
    PRIMARY KEY(user_id, place_id)
);