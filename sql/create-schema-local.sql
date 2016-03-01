create user 'lookdev'@'%' identified by 'lookdev'; 
create schema lookproto; 
grant all privileges on lookproto.* to 'lookdev'@'%';