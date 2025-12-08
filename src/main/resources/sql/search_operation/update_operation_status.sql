update lighthouse.search_operations
set status = :status,
    updated_at = now()
where operation_id = :operation_id;

