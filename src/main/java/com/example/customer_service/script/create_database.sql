-- utworzenie bazy danych
create database customer_service_db 
	character set utf8 
    collate utf8_polish_ci;

set @@global.time_zone = '+2:00';
    
-- utworznie użytkownika z uprawnieniami typu
-- create, drop, alter, insert, update, select, delete
create user 'customer_service_user'@'localhost' identified by 'qwe123';
grant 
	create, drop, alter, insert, update, select, delete
on 
	customer_service_db.*
to 
	'customer_service_user'@'localhost';