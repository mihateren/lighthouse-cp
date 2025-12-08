insert into lighthouse.team_members (team_id, volunteer_id, joined_at)
values (:team_id, :volunteer_id, now())
on conflict (team_id, volunteer_id) do nothing;

