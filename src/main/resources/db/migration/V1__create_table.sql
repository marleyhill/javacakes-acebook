CREATE TABLE  users (
    user_id VARCHAR UNIQUE,
    name VARCHAR(40),
    email VARCHAR(60) UNIQUE,
    password VARCHAR(80)
);

CREATE TABLE posts (
    post_id VARCHAR UNIQUE,
    user_id VARCHAR REFERENCES users(user_id),
    name VARCHAR (40),
    content VARCHAR(2000),
    time_stamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP(0)
);

CREATE TABLE post_likes (
    post_like_id VARCHAR UNIQUE,
    user_id VARCHAR REFERENCES users(user_id),
    post_id VARCHAR REFERENCES posts(post_id) ON DELETE CASCADE
);

CREATE TABLE comments (
    comment_id VARCHAR UNIQUE,
    post_id VARCHAR REFERENCES posts(post_id) ON DELETE CASCADE,
    user_id VARCHAR REFERENCES users(user_id),
    name VARCHAR(40),
    content VARCHAR(2000),
    time_stamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP(0)
);

CREATE TABLE comment_likes (
    comment_like_id VARCHAR UNIQUE,
    user_id VARCHAR REFERENCES users(user_id),
    comment_id VARCHAR REFERENCES comments(comment_id) ON DELETE CASCADE
);