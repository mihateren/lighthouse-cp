update lighthouse.volunteer_teams
set operation_id = :operation_id,
    team_leader_id = :team_leader_id,
    team_name = :team_name,
    updated_at = now()
where team_id = :team_id
returning team_id, operation_id, team_leader_id, team_name, created_at, updated_at;

