create table Users (
	userID varchar(20) primary key,
	password varchar(20) not null,
	realName varchar(30) not null,
	level int not null
);