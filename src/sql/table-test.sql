use springboot_practice_webservice;

drop table if exists test;

create table test (
    id bigint(20) not null auto_increment,
    content varchar(255) default null,
    primary key (id)
) engine=InnoDB;

insert into test (content) values ("테스트");

select * from test;
