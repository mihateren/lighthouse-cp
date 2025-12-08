update lighthouse.search_operations
set victim_id = :victim_id,
    admin_id = :admin_id,
    status = :status,
    updated_at = now()
where operation_id = :operation_id
returning operation_id, victim_id, admin_id, status, created_at, updated_at;

