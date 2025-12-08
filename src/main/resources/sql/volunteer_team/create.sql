insert into lighthouse.volunteer_teams (team_id, operation_id, team_leader_id, team_name, created_at, updated_at)
values (:team_id, :operation_id, :team_leader_id, :team_name, now(), now())
returning team_id, operation_id, team_leader_id, team_name, created_at, updated_at;

