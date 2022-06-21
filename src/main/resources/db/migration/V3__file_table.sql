create table if not exists ateupeonding_content.file (
    id uuid not null default gen_random_uuid() primary key,
    name text not null,
    resource text not null unique,
    post_id uuid not null references ateupeonding_content.post(id) on DELETE cascade,
    size int8 not null default 0,
    created_timestamp timestamp with time zone default now()
);

create index if not exists ateupeonding_content_file_post_id on ateupeonding_content.file(post_id);

alter table ateupeonding_content.post drop column if exists content_resource_link;