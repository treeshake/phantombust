ALTER TABLE ig_follower
ADD COLUMN ig_profile_id BIGINT,
ADD CONSTRAINT fk_ig_profile FOREIGN KEY (ig_profile_id) REFERENCES ig_profile (id);