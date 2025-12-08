insert into lighthouse.operation_status_updates (update_id, operation_id, volunteer_id, proposed_status, reason, admin_decision, admin_notes, created_at, updated_at)
values (:update_id, :operation_id, :volunteer_id, :proposed_status, :reason, 'pending', null, now(), now())
returning update_id, operation_id, volunteer_id, proposed_status, reason, admin_decision, admin_notes, created_at, decided_at, updated_at;

