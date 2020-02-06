CREATE TABLE  users (
    user_id SERIAL PRIMARY KEY,
    name VARCHAR(40),
    email VARCHAR(60),
    password VARCHAR(80)
);

CREATE TABLE posts (
    post_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id),
    content VARCHAR(2000),
    time_stamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE post_likes (
    post_like_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id),
    post_id INTEGER REFERENCES posts(post_id)
);

CREATE TABLE comments (
    comment_id SERIAL PRIMARY KEY,
    post_id INTEGER REFERENCES posts(post_id),
    user_id INTEGER REFERENCES users(user_id),
    content VARCHAR(2000),
    time_stamp TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE comment_likes (
    comment_like_id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(user_id),
    comment_id INTEGER REFERENCES comments(comment_id)
);