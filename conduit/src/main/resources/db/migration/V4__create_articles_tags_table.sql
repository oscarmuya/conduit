CREATE TABLE articles (
    id         BIGSERIAL PRIMARY KEY,
    slug       VARCHAR(255) UNIQUE,
    title      VARCHAR(255),
    description VARCHAR(255),
    body       TEXT,
    author_id  BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL
);

CREATE TABLE tags (
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) UNIQUE
);

CREATE TABLE article_tags (
    article_id BIGINT NOT NULL REFERENCES articles(id),
    tag_id     BIGINT NOT NULL REFERENCES tags(id),
    PRIMARY KEY (article_id, tag_id)
);

-- articles
CREATE INDEX idx_articles_author_id ON articles(author_id);
CREATE INDEX idx_articles_created_at ON articles(created_at DESC);


-- article_tags
CREATE INDEX idx_article_tags_tag_id ON article_tags(tag_id);
