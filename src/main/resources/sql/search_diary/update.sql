update lighthouse.search_diaries
set team_id = :team_id,
    volunteer_id = :volunteer_id,
    activity_type = :activity_type,
    description = :description,
    location = :location
where diary_id = :diary_id
returning diary_id, team_id, volunteer_id, activity_type, description, location, created_at;

