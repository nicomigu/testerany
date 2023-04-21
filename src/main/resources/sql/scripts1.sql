-- Drop user first if they exist
DROP USER if exists 'hcl_user'@'localhost' ;

-- Now create user with prop privileges
CREATE USER 'hcl_user'@'localhost' IDENTIFIED BY 'password';

GRANT ALL PRIVILEGES ON * . * TO 'hcl_user'@'localhost';