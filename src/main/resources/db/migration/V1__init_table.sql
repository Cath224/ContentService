create schema if not exists ateupeonding_content;


create table if not exists ateupeonding_content.post
(
    id                    uuid                     default gen_random_uuid() primary key,
    title                 text not null,
    content               varchar(4096),
    content_resource_link text,
    project_id            uuid not null,
    created_timestamp     timestamp with time zone default now()
);