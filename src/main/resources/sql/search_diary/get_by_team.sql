select diary_id, team_id, volunteer_id, activity_type, description, location, created_at
from lighthouse.search_diaries
where team_id = :team_id
order by created_at desc;

