/*
The various shop/artist profile information goes in this table
*/
CREATE TABLE VENDORS(
  PROFILE_ID BIGSERIAL PRIMARY KEY,
  FIRST_NAME VARCHAR(64),
  LAST_NAME VARCHAR(64),
  COMPANY VARCHAR(128) NOT NULL,
  EMAIL_ADDRESS VARCHAR(64) NOT NULL,
  ADDRESS VARCHAR(64),
  COUNTRY VARCHAR(32),
  STATE VARCHAR(12),
  PHONE_NUMBER VARCHAR(24),
  PROFILE_IMAGE_LINK VARCHAR
);

/*
Information on various posts goes in this table
*/
CREATE TABLE POSTS(
  POST_ID BIGSERIAL PRIMARY KEY,
  PROFILE_ID BIGINT NOT NULL,
  POST_DATE TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  LIKES INT
);

/*
the links to images of the posts go in this table
*/
CREATE TABLE IMAGES(
  IMAGE_ID BIGSERIAL PRIMARY KEY,
  PROFILE_ID BIGINT NOT NULL,
  POST_ID BIGINT NOT NULL,
  POST_IMAGE_LINK VARCHAR
);

/*
The various user profile information goes in this table
*/
CREATE TABLE USERS(
  USER_ID BIGSERIAL PRIMARY KEY
);

CREATE TABLE FOLLOWERS(
  PROFILE_ID BIGINT NOT NULL,
  USER_ID BIGINT NOT NULL
);


ALTER TABLE POSTS ADD FOREIGN KEY (PROFILE_ID) REFERENCES VENDORS(PROFILE_ID);
ALTER TABLE IMAGES ADD FOREIGN KEY (POST_ID) REFERENCES POSTS(POST_ID);
ALTER TABLE IMAGES ADD FOREIGN KEY (PROFILE_ID) REFERENCES VENDORS(PROFILE_ID);
CREATE INDEX IDX_POST_DATE ON POSTS(POST_DATE);
ALTER TABLE FOLLOWERS ADD FOREIGN KEY (USER_ID) REFERENCES USERS(USER_ID);
ALTER TABLE FOLLOWERS ADD FOREIGN KEY (PROFILE_ID) REFERENCES VENDORS(PROFILE_ID);
