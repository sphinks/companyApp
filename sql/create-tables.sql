drop table company;
create table company (
  id integer auto_increment primary key,
  name varchar(256),
  address varchar(256),
  city varchar(256),
  country varchar(256),
  email varchar(256),
  phone_number varchar(256)
);

drop table beneficial;
create table beneficial (
  id integer auto_increment primary key,
  company_id integer,
  name varchar(256)
);
