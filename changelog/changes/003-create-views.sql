-- liquibase formatted sql
-- changeset admin:3

-- представление: статистика по операциям поиска
create or replace view lighthouse.v_operations_statistics as
select 
    so.operation_id,
    v.first_name || ' ' || v.last_name as victim_name,
    v.city as victim_city,
    so.status,
    so.created_at as operation_started,
    so.updated_at as last_updated,
    count(distinct vt.team_id) as teams_count,
    count(distinct tm.volunteer_id) as volunteers_count,
    count(distinct sd.diary_id) as diary_entries_count,
    max(sd.created_at) as last_activity_date
from lighthouse.search_operations so
join lighthouse.victims v on so.victim_id = v.victim_id
left join lighthouse.volunteer_teams vt on so.operation_id = vt.operation_id
left join lighthouse.team_members tm on vt.team_id = tm.team_id
left join lighthouse.search_diaries sd on vt.team_id = sd.team_id
group by so.operation_id, v.first_name, v.last_name, v.city, so.status, so.created_at, so.updated_at;

comment on view lighthouse.v_operations_statistics is 'Агрегированная статистика по операциям поиска с количеством команд, волонтеров и записей в дневнике';

-- представление: активность волонтеров
create or replace view lighthouse.v_volunteer_activity as
select 
    vol.volunteer_id,
    vol.first_name || ' ' || vol.last_name as volunteer_name,
    vol.email,
    vc.title as center_name,
    count(distinct tm.team_id) as teams_participated,
    count(distinct so.operation_id) as operations_participated,
    count(distinct sd.diary_id) as diary_entries_count,
    count(distinct case when vt.team_leader_id = vol.volunteer_id then vt.team_id end) as teams_led,
    min(sd.created_at) as first_activity_date,
    max(sd.created_at) as last_activity_date
from lighthouse.volunteers vol
join lighthouse.volunteer_centers vc on vol.center_id = vc.center_id
left join lighthouse.team_members tm on vol.volunteer_id = tm.volunteer_id
left join lighthouse.volunteer_teams vt on tm.team_id = vt.team_id
left join lighthouse.search_operations so on vt.operation_id = so.operation_id
left join lighthouse.search_diaries sd on tm.team_id = sd.team_id and sd.volunteer_id = vol.volunteer_id
where vol.is_active = true
group by vol.volunteer_id, vol.first_name, vol.last_name, vol.email, vc.title;

comment on view lighthouse.v_volunteer_activity is 'Активность волонтеров: количество участий в командах, операциях и записях в дневнике';

-- представление: статистика по командам
create or replace view lighthouse.v_teams_statistics as
select 
    vt.team_id,
    vt.team_name,
    so.operation_id,
    v.first_name || ' ' || v.last_name as victim_name,
    vol.first_name || ' ' || vol.last_name as team_leader_name,
    count(distinct tm.volunteer_id) as members_count,
    count(distinct sd.diary_id) as diary_entries_count,
    count(distinct sd.activity_type) as unique_activity_types,
    min(sd.created_at) as first_activity_date,
    max(sd.created_at) as last_activity_date,
    vt.created_at as team_created_at
from lighthouse.volunteer_teams vt
join lighthouse.search_operations so on vt.operation_id = so.operation_id
join lighthouse.victims v on so.victim_id = v.victim_id
join lighthouse.volunteers vol on vt.team_leader_id = vol.volunteer_id
left join lighthouse.team_members tm on vt.team_id = tm.team_id
left join lighthouse.search_diaries sd on vt.team_id = sd.team_id
group by vt.team_id, vt.team_name, so.operation_id, v.first_name, v.last_name, 
         vol.first_name, vol.last_name, vt.created_at;

comment on view lighthouse.v_teams_statistics is 'Статистика по командам: количество участников, записей в дневнике и типов активности';

-- представление: статусы операций и решения админов
create or replace view lighthouse.v_operation_status_history as
select 
    so.operation_id,
    v.first_name || ' ' || v.last_name as victim_name,
    so.status as current_status,
    osu.update_id,
    vol.first_name || ' ' || vol.last_name as proposed_by_volunteer,
    osu.proposed_status,
    osu.reason,
    osu.admin_decision,
    osu.admin_notes,
    osu.created_at as proposal_date,
    osu.decided_at,
    case 
        when osu.admin_decision = 'approved' then 'Одобрено'
        when osu.admin_decision = 'rejected' then 'Отклонено'
        when osu.admin_decision = 'pending' then 'Ожидает решения'
        else 'Неизвестно'
    end as decision_status_ru
from lighthouse.search_operations so
join lighthouse.victims v on so.victim_id = v.victim_id
left join lighthouse.operation_status_updates osu on so.operation_id = osu.operation_id
left join lighthouse.volunteers vol on osu.volunteer_id = vol.volunteer_id
order by so.operation_id, osu.created_at desc;

comment on view lighthouse.v_operation_status_history is 'История изменений статусов операций с решениями админов';

