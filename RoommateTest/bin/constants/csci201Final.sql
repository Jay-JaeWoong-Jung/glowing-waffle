drop database if exists newsql;
create database newsql;
use newsql;

CREATE TABLE user (
  username varchar(50) primary key not null,
  password varchar(50) not null,
  first_name varchar(50) not null,
  last_name varchar(50) not null,
  cell_number bigint(11) not null,
  emerg_number bigint(11) not null,
  email varchar(50) not null,
  house_address varchar(50),
  house_handle varchar(11),
  permanant_address varchar(50),
  venmo_handle varchar(11),
  class_calendar_id varchar(200),
  social_calendar_id varchar(200),
  group_calendar_id varchar(200),
  status varchar(50)
);


INSERT INTO user (username, password, first_name, last_name, cell_number, emerg_number, email, house_address, house_handle, status) VALUES ('aaa','aaa', 'Jay', 'Jung', '1234567890','0987654321','jaewoonj@usc.edu', '1241 W 37th PL, LA, CA, 90007, USA', 'main', 'InTheRoom');
