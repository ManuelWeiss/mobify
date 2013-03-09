# items schema
 
# --- !Ups

CREATE TABLE items (
    id varchar(255),
    data varchar(1023) not null,
    priority bigint not null default 0,
    inserted SERIAL
);
 
# --- !Downs
 
DROP TABLE items;
