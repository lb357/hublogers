CREATE TABLE IF NOT EXISTS users (
    id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username varchar(16) UNIQUE NOT NULL,
    email varchar UNIQUE NOT NULL,
    password_hash varchar NOT NULL,
    status varchar(100) DEFAULT 'Хаблогер',

    CONSTRAINT users_check_username_not_empty CHECK (trim(username) <> ''),
    CONSTRAINT users_check_email_not_empty CHECK (trim(email) <> ''),
    CONSTRAINT users_check_password_hash_not_empty CHECK (trim(password_hash) <> ''),

    CONSTRAINT users_check_email_valid CHECK (email LIKE '%@%.%')
);

CREATE TABLE IF NOT EXISTS hubs (
    id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    creator_id int DEFAULT NULL,
    hubname varchar(100) UNIQUE NOT NULL,
    description varchar NOT NULL,

    CONSTRAINT hubs_check_hubname_not_empty CHECK (trim(hubname) <> ''),
    CONSTRAINT hubs_check_description_not_empty CHECK (trim(description) <> ''),

    CONSTRAINT fk_hubs_users FOREIGN KEY (creator_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS posts (
    id int PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    author_id int NOT NULL,
    hub_id int,
    label varchar(100) UNIQUE NOT NULL,
    content text UNIQUE NOT NULL,
    creation_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP

    CONSTRAINT posts_check_label_not_empty CHECK (label IS NOT NULL AND trim(label) <> ''),

    CONSTRAINT fk_posts_users FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_posts_hubs FOREIGN KEY (hub_id) REFERENCES hubs(id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS votes (
    post_id int,
    voter_id int,
    vote boolean NOT NULL,

    CONSTRAINT pk_votes PRIMARY KEY (post_id, voter_id),

    CONSTRAINT fk_votes_users FOREIGN KEY (voter_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_votes_posts FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);


CREATE TABLE IF NOT EXISTS sessions (
    auth_token varchar PRIMARY KEY UNIQUE NOT NULL,
    user_id int NOT NULL,
    auth_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_sessions_user_id FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);