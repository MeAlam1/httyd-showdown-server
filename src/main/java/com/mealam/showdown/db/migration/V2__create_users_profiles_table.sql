CREATE TABLE user_profiles
(
    id           BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id      BIGINT NOT NULL UNIQUE,
    display_name VARCHAR(32),
    description  VARCHAR(256),
    image_url    VARCHAR(512),
    CONSTRAINT fk_user_profiles_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);