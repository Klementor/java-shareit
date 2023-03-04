CREATE TABLE IF NOT EXISTS users
(
    user_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name    VARCHAR(50)         NOT NULL,
    email   VARCHAR(100) UNIQUE NOT NULL
);

CREATE TABLE IF NOT EXISTS items
(
    item_id      BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name         VARCHAR(200)  NOT NULL,
    description  VARCHAR(1000) NOT NULL,
    available BOOLEAN       NOT NULL,
    owner_id     BIGINT REFERENCES users (user_id)
);

CREATE TABLE IF NOT EXISTS bookings
(
    booking_id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start_time TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    end_time   TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    item_id    BIGINT REFERENCES items (item_id),
    booker_id  BIGINT REFERENCES users (user_id),
    status     VARCHAR(50)
);