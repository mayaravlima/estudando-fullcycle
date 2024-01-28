CREATE TABLE categories
(
    id          VARCHAR(40)     NOT NULL PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(4000),
    active      BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at  DATETIME(6)  NOT NULL,
    updated_at  DATETIME(6)  NOT NULL,
    deleted_at  DATETIME(6)  NULL
);

CREATE TABLE videos (
                        id CHAR(40) NOT NULL PRIMARY KEY,
                        title VARCHAR(255) NOT NULL,
                        description VARCHAR(1000) NOT NULL,
                        url VARCHAR(255) NOT NULL,
                        created_at DATETIME(6) NOT NULL
);

CREATE TABLE videos_categories (
                                   video_id CHAR(40) NOT NULL,
                                   category_id CHAR(40) NOT NULL,
                                   CONSTRAINT idx_vcs_video_category UNIQUE (video_id, category_id),
                                   CONSTRAINT fk_vcs_video_id FOREIGN KEY (video_id) REFERENCES videos (id),
                                   CONSTRAINT fk_vcs_category_id FOREIGN KEY (category_id) REFERENCES categories (id)
);