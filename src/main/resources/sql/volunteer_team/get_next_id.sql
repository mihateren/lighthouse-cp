select coalesce(max(team_id), 0) + 1
from lighthouse.volunteer_teams;

