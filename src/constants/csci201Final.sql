drop database if exists csci201Final;
create database csci201Final;
use csci201Final;

CREATE TABLE user (
  id int(11) primary key not null auto_increment,
  username varchar(50) not null,
  password varchar(50) not null,
  first_name varchar(50) not null,
  last_name varchar(50) not null,
  cell_number int(11) not null,
  emerg_number int(11) not null,
  email varchar(50) not null,
  address varchar(50) not null,
  house_handle varchar(11) not null,
  permanant_address varchar(50),
  venmo_handle varchar(11),
  class_calendar_id varchar(50),
  social_calendar_id varchar(50),
  group_calendar_id varchar(50)
  
);


INSERT INTO user (username, password, first_name, last_name, cell_number, emerg_number, email, address, house_handle) VALUES ('aaa','aaa', 'Jay', 'Jung', '1234567890','0987654321','jaewoonj@usc.edu', '1241 W 37th PL, LA, CA, 90007, USA', 'main');
