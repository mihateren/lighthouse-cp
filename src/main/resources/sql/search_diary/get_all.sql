select diary_id, team_id, volunteer_id, activity_type, description, location, created_at
from lighthouse.search_diaries
order by created_at desc;

