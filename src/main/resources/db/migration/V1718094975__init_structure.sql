-- --------------------------------------------
-- Table user
-- --------------------------------------------
CREATE TABLE user (
  id bigint NOT NULL AUTO_INCREMENT,
  confirm_password varchar(255) NOT NULL,
  email varchar(255) NOT NULL,
  first_name varchar(255) NOT NULL,
  last_name varchar(255) NOT NULL,
  password varchar(255) NOT NULL,
  phone_number varchar(255) DEFAULT NULL,
  pin varchar(255) DEFAULT NULL,
  token varchar(255) DEFAULT NULL,
  token_creation_date timestamp NULL DEFAULT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uk_user_email (email)
);
