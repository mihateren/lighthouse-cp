update lighthouse.operation_status_updates
set admin_decision = :admin_decision,
    admin_notes = :admin_notes,
    decided_at = case when :admin_decision != 'pending' then now() else decided_at end,
    updated_at = now()
where update_id = :update_id
returning update_id, operation_id, volunteer_id, proposed_status, reason, admin_decision, admin_notes, created_at, decided_at, updated_at;

