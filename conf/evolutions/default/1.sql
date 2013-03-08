# items schema
 
# --- !Ups

CREATE TABLE items (
    id varchar(255) PRIMARY KEY,
    data varchar(1023) not null,
    priority bigint not null default 0,
    inserted bigint not null AUTO_INCREMENT 
);
 
# --- !Downs
 
DROP TABLE items;