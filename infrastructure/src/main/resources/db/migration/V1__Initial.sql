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

CREATE TABLE users (
                        id CHAR(40) NOT NULL PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL,
                        created_at DATETIME(6) NOT NULL
);

CREATE TABLE videos_categories (
                                   video_id CHAR(40) NOT NULL,
                                   category_id CHAR(40) NOT NULL,
                                   CONSTRAINT idx_vcs_video_category UNIQUE (video_id, category_id),
                                   CONSTRAINT fk_vcs_video_c_id FOREIGN KEY (video_id) REFERENCES videos (id),
                                   CONSTRAINT fk_vcs_category_id FOREIGN KEY (category_id) REFERENCES categories (id)
);

CREATE TABLE users_videos (
                                   user_id CHAR(40) NOT NULL,
                                   video_id CHAR(40) NOT NULL,
                                   CONSTRAINT idx_vcs_user_video UNIQUE (user_id, video_id),
                                   CONSTRAINT fk_vcs_video_u_id FOREIGN KEY (video_id) REFERENCES videos (id),
                                   CONSTRAINT fk_vcs_user_id FOREIGN KEY (user_id) REFERENCES users (id)
);