select team_id, operation_id, team_leader_id, team_name, created_at, updated_at
from lighthouse.volunteer_teams
where team_id = :team_id;

