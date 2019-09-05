
create table entity (id bigint not null, data varchar(255), salt bigint, primary key (id));

create table hibernate_sequence (next_val bigint);

insert into hibernate_sequence values ( 1 );