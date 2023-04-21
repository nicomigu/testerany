create database mutualfundtest;
use mutualfund;

create table CUSTOMER_CREDENTIALS(
	USER_ID varchar(64) not null unique,
    PASSWORD varchar(64) not null unique
);

create table CUSTOMER(
	CUSTOMER_ID int not null auto_increment primary key,
    FIRST_NAME varchar(64) not null,
    LAST_NAME varchar(64) not null,
    EMAIL_ID varchar(64) not null unique,
    VERIFICATION_STATUS varchar(64) not null,
    BIRTH_DATE date
);

create table CUSTOMER_OTP(
	ID int not null auto_increment primary key,
    CUSTOMER_ID int not null,
    OTP int not null,
    foreign key (CUSTOMER_ID) references CUSTOMER(CUSTOMER_ID)
);

create table CUSTOMER_TRANSACTION_TYPE(
	TYPE_ID int not null primary key,
    DESCRIPTION varchar(64) 
);

create table CUSTOMER_WALLET(
	WALLET_ID int not null auto_increment primary key,
    CUSTOMER_ID int not null,
    WALLET_BALANCE int default 0,
    TRANSACTION_DATE date,
	foreign key (CUSTOMER_ID) references CUSTOMER(CUSTOMER_ID)
);

create table CUSTOMER_WALLET_HISTORY(
	ID int not null auto_increment primary key,
    WALLET_ID int not null,
    CUSTOMER_ID int not null,
    WALLET_AMOUNT int,
    TRANSACTION_TYPE_ID int,
    TRANSACTION_DATE date,
    foreign key (CUSTOMER_ID) references CUSTOMER(CUSTOMER_ID),
    foreign key (WALLET_ID) references CUSTOMER_WALLET(WALLET_ID),
    foreign key (TRANSACTION_TYPE_ID) references CUSTOMER_TRANSACTION_TYPE(TYPE_ID)
);

create table mfdetails(
schema_id int NOT NULL primary key auto_increment,
fund_house varchar(255) not null,
symbol varchar(255) not null,
schema_name varchar(255) not null,
schema_category varchar(255) not null,
description varchar(255) not null,
curr_price double(5,2) not null,
delta double(5,2) not null
);


create table mfdetailhistory(
Id int not null primary key auto_increment,
date date not null,
schema_id int not null,
nav double(5,2) not null
);

create table CUSTOMER_WISHLIST(
	WISHLIST_ID int not null auto_increment primary key,
    CUSTOMER_ID int,
    MF_NAME varchar(64),
    MF_FUND_HOUSE varchar(64),
    MF_SCHEMA_ID int,
    foreign key (CUSTOMER_ID) references CUSTOMER(CUSTOMER_ID),
    foreign key (MF_SCHEMA_ID) references mfdetails(schema_id)
);

create table CUSTOMER_PORTFOLIO(
	PORTFOLIO_ID int not null auto_increment primary key,
    CUSTOMER_ID int,
    MF_NAME varchar(64),
    MF_FUND_HOUSE varchar(64),
    MF_UNITS_AVAILABLE int default 0,
    CURRENCY varchar(64) not null,
    TRANSACTION_DATE date,
    foreign key (CUSTOMER_ID) references CUSTOMER(CUSTOMER_ID)
);

create table CUSTOMER_MF_HISTORY(
	ID int not null auto_increment primary key,
    PORTFOLIO_ID int,
	CUSTOMER_ID int,
	MF_NAME varchar(64),
    MF_FUND_HOUSE varchar(64),
    MF_UNITS int,
    CURRENCY varchar(64) not null,
    MF_TRANSACTION_TYPE int,
    TRANSACTION_DATE date,
    foreign key (PORTFOLIO_ID) references CUSTOMER_PORTFOLIO(PORTFOLIO_ID),
    foreign key (CUSTOMER_ID) references CUSTOMER(CUSTOMER_ID),
    foreign key (MF_TRANSACTION_TYPE) references CUSTOMER_TRANSACTION_TYPE(TYPE_ID)
);

