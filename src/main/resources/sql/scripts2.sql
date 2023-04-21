create database IF NOT EXISTS mutual_fund;
use mutual_fund;

DROP TABLE IF EXISTS `customer`;



CREATE TABLE `customer`
(
    `id`    int NOT NULL AUTO_INCREMENT,
    `fname` varchar(45),
    `lname` varchar(45),
    `pwd`   varchar(450),
    `email` varchar(45),
    `date_of_birth` date,
    PRIMARY KEY (`id`)
);



INSERT INTO `customer` (`fname`, `lname`, `pwd`, `email`, `date_of_birth`)
VALUES ('Justin', 'Truduo', 'pwd', 'johndoe@example.com', '0000-00-00');