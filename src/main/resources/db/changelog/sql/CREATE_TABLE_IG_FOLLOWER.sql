
CREATE TABLE ig_follower (
    id serial,
    source_profile VARCHAR(255) NOT NULL,
    ig_user_id BIGINT NOT NULL,
    CONSTRAINT unique_source_profile_ig_user UNIQUE (source_profile, ig_user_id),
    CONSTRAINT fk_ig_user FOREIGN KEY (ig_user_id) REFERENCES ig_user (id)
);