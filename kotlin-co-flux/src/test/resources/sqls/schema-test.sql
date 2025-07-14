-- Test Only Table Generation

create table board(
                      id SERIAL primary key,
                      title varchar(100) not null,
                      content text not null,
                      created_at timestamp(6) ,
                      updated_at timestamp(6)
);