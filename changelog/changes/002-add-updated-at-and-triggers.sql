-- liquibase formatted sql
-- changeset admin:2

-- добавление столбца updated_at в таблицы для аудита
alter table lighthouse.victims add column updated_at timestamp;
alter table lighthouse.volunteers add column updated_at timestamp;
alter table lighthouse.volunteer_teams add column updated_at timestamp;
alter table lighthouse.operation_status_updates add column updated_at timestamp;

-- установка начального значения для существующих записей
update lighthouse.victims set updated_at = now() where updated_at is null;
update lighthouse.volunteers set updated_at = now() where updated_at is null;
update lighthouse.volunteer_teams set updated_at = created_at where updated_at is null;
update lighthouse.operation_status_updates set updated_at = created_at where updated_at is null;

-- установка not null после заполнения
alter table lighthouse.victims alter column updated_at set not null;
alter table lighthouse.volunteers alter column updated_at set not null;
alter table lighthouse.volunteer_teams alter column updated_at set not null;
alter table lighthouse.operation_status_updates alter column updated_at set not null;

-- установка значения по умолчанию
alter table lighthouse.victims alter column updated_at set default now();
alter table lighthouse.volunteers alter column updated_at set default now();
alter table lighthouse.volunteer_teams alter column updated_at set default now();
alter table lighthouse.operation_status_updates alter column updated_at set default now();

-- функция для автоматического обновления updated_at
create or replace function lighthouse.update_updated_at_column()
returns trigger as $$
begin
    new.updated_at = now();
    return new;
end;
$$ language plpgsql;

-- триггеры для автоматического обновления updated_at при update
create trigger trg_victims_updated_at
    before update on lighthouse.victims
    for each row
    execute function lighthouse.update_updated_at_column();

create trigger trg_volunteers_updated_at
    before update on lighthouse.volunteers
    for each row
    execute function lighthouse.update_updated_at_column();

create trigger trg_volunteer_teams_updated_at
    before update on lighthouse.volunteer_teams
    for each row
    execute function lighthouse.update_updated_at_column();

create trigger trg_search_operations_updated_at
    before update on lighthouse.search_operations
    for each row
    execute function lighthouse.update_updated_at_column();

create trigger trg_operation_status_updates_updated_at
    before update on lighthouse.operation_status_updates
    for each row
    execute function lighthouse.update_updated_at_column();

-- триггер для автоматического обновления decided_at при изменении admin_decision
create or replace function lighthouse.update_decided_at()
returns trigger as $$
begin
    if old.admin_decision = 'pending' and new.admin_decision != 'pending' then
        new.decided_at = now();
    end if;
    return new;
end;
$$ language plpgsql;

create trigger trg_operation_status_updates_decided_at
    before update on lighthouse.operation_status_updates
    for each row
    execute function lighthouse.update_decided_at();

