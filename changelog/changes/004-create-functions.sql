-- liquibase formatted sql
-- changeset admin:4

-- скалярная функция: расчет рейтинга волонтера
create or replace function lighthouse.calculate_volunteer_rating(p_volunteer_id bigint)
returns numeric as '
declare
    v_rating numeric;
    v_operations_count integer;
    v_teams_led_count integer;
    v_diary_entries_count integer;
begin
    -- количество операций, в которых участвовал волонтер
    select count(distinct so.operation_id) into v_operations_count
    from lighthouse.team_members tm
    join lighthouse.volunteer_teams vt on tm.team_id = vt.team_id
    join lighthouse.search_operations so on vt.operation_id = so.operation_id
    where tm.volunteer_id = p_volunteer_id;
    
    -- количество команд, которыми руководил волонтер
    select count(*) into v_teams_led_count
    from lighthouse.volunteer_teams
    where team_leader_id = p_volunteer_id;
    
    -- количество записей в дневнике
    select count(*) into v_diary_entries_count
    from lighthouse.search_diaries
    where volunteer_id = p_volunteer_id;
    
    -- расчет рейтинга: операции * 10 + команды * 20 + записи * 2
    v_rating := (v_operations_count * 10.0) + (v_teams_led_count * 20.0) + (v_diary_entries_count * 2.0);
    
    return coalesce(v_rating, 0);
end
' language plpgsql;

comment on function lighthouse.calculate_volunteer_rating(bigint) is 'Рассчитывает рейтинг волонтера на основе участия в операциях, руководства командами и записей в дневнике';

-- скалярная функция: количество активных операций по центру
create or replace function lighthouse.count_active_operations_by_center(p_center_id bigint)
returns integer as '
declare
    v_count integer;
begin
    select count(*) into v_count
    from lighthouse.search_operations so
    join lighthouse.admins a on so.admin_id = a.admin_id
    where a.center_id = p_center_id
      and so.status = ''active'';
    
    return coalesce(v_count, 0);
end
' language plpgsql;

comment on function lighthouse.count_active_operations_by_center(bigint) is 'Возвращает количество активных операций поиска для указанного центра';

-- скалярная функция: средний возраст пропавших по городу
create or replace function lighthouse.avg_victim_age_by_city(p_city varchar)
returns numeric as '
declare
    v_avg_age numeric;
begin
    select avg(age) into v_avg_age
    from lighthouse.victims
    where city = p_city
      and age is not null;
    
    return round(coalesce(v_avg_age, 0), 2);
end
' language plpgsql;

comment on function lighthouse.avg_victim_age_by_city(varchar) is 'Возвращает средний возраст пропавших людей в указанном городе';

-- табличная функция: отчет по операциям за период
create or replace function lighthouse.get_operations_report(
    p_start_date timestamp default null,
    p_end_date timestamp default null,
    p_status varchar default null
)
returns table (
    operation_id bigint,
    victim_name text,
    victim_city varchar,
    status varchar,
    created_at timestamp,
    teams_count bigint,
    volunteers_count bigint,
    diary_entries_count bigint,
    days_active integer
) as '
begin
    return query
    select 
        so.operation_id,
        v.first_name || '' '' || v.last_name as victim_name,
        v.city as victim_city,
        so.status,
        so.created_at,
        count(distinct vt.team_id) as teams_count,
        count(distinct tm.volunteer_id) as volunteers_count,
        count(distinct sd.diary_id) as diary_entries_count,
        extract(day from (coalesce(so.updated_at, now()) - so.created_at))::integer as days_active
    from lighthouse.search_operations so
    join lighthouse.victims v on so.victim_id = v.victim_id
    left join lighthouse.volunteer_teams vt on so.operation_id = vt.operation_id
    left join lighthouse.team_members tm on vt.team_id = tm.team_id
    left join lighthouse.search_diaries sd on vt.team_id = sd.team_id
    where (p_start_date is null or so.created_at >= p_start_date)
      and (p_end_date is null or so.created_at <= p_end_date)
      and (p_status is null or so.status = p_status)
    group by so.operation_id, v.first_name, v.last_name, v.city, so.status, so.created_at, so.updated_at
    order by so.created_at desc;
end
' language plpgsql;

comment on function lighthouse.get_operations_report(timestamp, timestamp, varchar) is 'Возвращает отчет по операциям поиска за указанный период с фильтрацией по статусу';

-- табличная функция: топ волонтеров по активности
create or replace function lighthouse.get_top_volunteers(
    p_limit integer default 10,
    p_center_id bigint default null
)
returns table (
    volunteer_id bigint,
    volunteer_name text,
    center_name varchar,
    rating numeric,
    operations_count bigint,
    teams_led_count bigint,
    diary_entries_count bigint
) as '
begin
    return query
    select 
        vol.volunteer_id,
        vol.first_name || '' '' || vol.last_name as volunteer_name,
        vc.title as center_name,
        lighthouse.calculate_volunteer_rating(vol.volunteer_id) as rating,
        count(distinct so.operation_id) as operations_count,
        count(distinct case when vt.team_leader_id = vol.volunteer_id then vt.team_id end) as teams_led_count,
        count(distinct sd.diary_id) as diary_entries_count
    from lighthouse.volunteers vol
    join lighthouse.volunteer_centers vc on vol.center_id = vc.center_id
    left join lighthouse.team_members tm on vol.volunteer_id = tm.volunteer_id
    left join lighthouse.volunteer_teams vt on tm.team_id = vt.team_id
    left join lighthouse.search_operations so on vt.operation_id = so.operation_id
    left join lighthouse.search_diaries sd on tm.team_id = sd.team_id and sd.volunteer_id = vol.volunteer_id
    where vol.is_active = true
      and (p_center_id is null or vol.center_id = p_center_id)
    group by vol.volunteer_id, vol.first_name, vol.last_name, vc.title
    order by rating desc, operations_count desc
    limit p_limit;
end
' language plpgsql;

comment on function lighthouse.get_top_volunteers(integer, bigint) is 'Возвращает топ волонтеров по рейтингу с возможностью фильтрации по центру';
