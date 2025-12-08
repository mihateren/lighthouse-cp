insert into lighthouse.search_diaries (diary_id, team_id, volunteer_id, activity_type, description, location, created_at)
values (:diary_id, :team_id, :volunteer_id, :activity_type, :description, :location, now())
returning diary_id, team_id, volunteer_id, activity_type, description, location, created_at;

