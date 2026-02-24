CREATE TABLE follows (
    id BIGSERIAL PRIMARY KEY,    
    follower_id BIGINT NOT NULL,
    following_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    -- Foreign Key Constraints
    CONSTRAINT fk_follower 
        FOREIGN KEY (follower_id) 
        REFERENCES users(id) 
        ON DELETE CASCADE,

    CONSTRAINT fk_following 
        FOREIGN KEY (following_id) 
        REFERENCES users(id) 
        ON DELETE CASCADE,

    -- Unique Constraint 
    CONSTRAINT uq_follower_following 
        UNIQUE (follower_id, following_id)
);


-- Index for fetches like: find users following me
CREATE INDEX idx_follows_following_id ON follows(following_id);
