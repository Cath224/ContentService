drop index if exists ateupeonding_content_file_post_id;

alter table ateupeonding_content.file
    drop column if exists post_id cascade,
    add column if not exists ref_id   uuid,
    add column if not exists ref_type text;

create index if not exists ateupeonding_content_file_ref_id_ref_type on ateupeonding_content.file (ref_id, ref_type);

create unique index if not exists ateupeonding_content_file_ref_id_ref_type_project on ateupeonding_content.file (ref_id)
    where file.ref_type = 'project';